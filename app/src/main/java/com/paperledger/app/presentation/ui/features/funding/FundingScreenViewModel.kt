package com.paperledger.app.presentation.ui.features.funding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paperledger.app.core.Routes
import com.paperledger.app.core.UIEvent
import com.paperledger.app.core.mapError
import com.paperledger.app.core.mapErrorMessage
import com.paperledger.app.data.remote.dto.funding.request.FundingRequestDTO
import com.paperledger.app.domain.usecase.ach.GetACHRelationshipIdUseCase
import com.paperledger.app.domain.usecase.auth.GetUserIdUseCase
import com.paperledger.app.domain.usecase.funding.RequestTransferUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FundingScreenViewModel @Inject constructor(
    private val requestTransferUseCase: RequestTransferUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getACHRelationshipIdUseCase: GetACHRelationshipIdUseCase
): ViewModel() {
    private val _state = MutableStateFlow(FundingScreenState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            _state.value = _state.value.copy(
                isLoading = true,
                transferType = "ach",
                direction = "INCOMING"
            )
            getACHRelationshipIdUseCase.invoke(getUserIdUseCase() ?: "").fold(
                onSuccess = { relationshipId ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            relationshipId = relationshipId,
                            error = null
                        )
                    }
                },
                onFailure = { e ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = mapErrorMessage(e)
                        )
                    }
                    sendUIEvent(UIEvent.ShowSnackBar(message = _state.value.error ?: ""))
                }
            )
        }

    }
    fun requestTransfer(){
        viewModelScope.launch {
            if(_state.value.amount.isBlank()){
                _state.update {
                    it.copy(
                        error = "Please enter an amount"
                    )
                }
            }
            if(_state.value.relationshipId.isBlank()){
                _state.value = _state.value.copy(
                    error = "There is no relationship ID, kindly refresh"
                )
            }else{
                _state.update {
                    it.copy(
                        isLoading = true,
                        error = null
                    )
                }
                requestTransferUseCase.invoke(FundingRequestDTO(
                    amount = _state.value.amount,
                    direction = _state.value.direction,
                    relationshipId = _state.value.relationshipId,
                    transferType = _state.value.transferType
                ),
                    getUserIdUseCase() ?: "").fold(
                    onSuccess = {
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = null
                            )
                        }
                        sendUIEvent(UIEvent.ShowSnackBar(message = "Transfer ongoing, balance will reflect in 10 minutes"))
                        sendUIEvent(UIEvent.Navigate(Routes.HOME))
                    },
                    onFailure = { e ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = mapErrorMessage(e)
                            )
                        }
                        sendUIEvent(UIEvent.ShowSnackBar(message = _state.value.error ?: ""))
                    }
                )
            }
        }
    }

    fun onEvent(event: FundingScreenEvent){
        when(event){
            is FundingScreenEvent.OnSubmit -> {
                requestTransfer()
            }
            is FundingScreenEvent.OnAmountChange -> {
                _state.update {
                    it.copy(
                        amount = event.amount
                    )
                }
            }
        }
    }

    private fun sendUIEvent(event: UIEvent){
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}