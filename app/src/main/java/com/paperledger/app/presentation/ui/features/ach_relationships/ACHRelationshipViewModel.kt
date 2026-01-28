package com.paperledger.app.presentation.ui.features.ach_relationships

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paperledger.app.core.Routes
import com.paperledger.app.core.UIEvent
import com.paperledger.app.core.mapErrorMessage
import com.paperledger.app.data.remote.dto.ach.request.ACHRelationshipsRequestDTO
import com.paperledger.app.domain.usecase.ach.CreateACHRelationshipUseCase
import com.paperledger.app.domain.usecase.ach.StoreACHRelationshipTokenUseCase
import com.paperledger.app.domain.usecase.auth.GetUserIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ACHRelationshipViewModel @Inject constructor(
    private val createACHRelationshipUseCase: CreateACHRelationshipUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val storeACHRelationshipTokenUseCase: StoreACHRelationshipTokenUseCase): ViewModel() {
    private val _state = MutableStateFlow(ACHRelationshipState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private fun createACHRelationship(){
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true
                )
            }
            createACHRelationshipUseCase.invoke(getUserIdUseCase() ?: "",
                ACHRelationshipsRequestDTO(
                    nickname = _state.value.nickname,
                    accountOwnerName = _state.value.ownerName,
                    bankAccountType = _state.value.accountType,
                    bankAccountNumber = _state.value.accountNumber,
                    bankRoutingNumber = _state.value.routingNumber
                )).fold(
                    onSuccess = {
                        _state.update {
                            it.copy(
                                isLoading = false
                            )
                        }
                        storeACHRelationshipTokenUseCase.invoke("ach_relationship_token")
                        sendUIEvent(UIEvent.ShowSnackBar(message = "ACH Relationship Created"))
                        sendUIEvent(UIEvent.Navigate(Routes.FUNDING_SCREEN))
                    },
                    onFailure = { e ->
                        _state.update {
                            it.copy(
                                isLoading = false
                            )
                        }
                        sendUIEvent(UIEvent.ShowSnackBar(message = mapErrorMessage(e), action = "Retry"))
                    }
                )
        }
    }
    
    fun onEvent(event: ACHRelationshipEvent){
        when(event){
            is ACHRelationshipEvent.OnSubmit -> {
                if(_state.value.ownerName.isNotBlank() && _state.value.accountNumber.isNotBlank() && _state.value.routingNumber.isNotBlank() && _state.value.nickname.isNotBlank() && _state.value.accountType.isNotBlank()){
                    createACHRelationship()
                }else{
                    sendUIEvent(UIEvent.ShowSnackBar(message = "Please fill in all fields"))
                }
            }

            is ACHRelationshipEvent.OnAccountNumberChange -> {
                _state.update { 
                    it.copy(
                        accountNumber = event.accountNumber
                    )
                }
            }
            is ACHRelationshipEvent.OnAccountTypeChange -> {
                _state.update { 
                    it.copy(
                        accountType = event.accountType
                    )
                }
            }
            is ACHRelationshipEvent.OnNickNameChange -> {
                _state.update { 
                    it.copy(
                        nickname = event.nickname
                    )
                }
            }
            is ACHRelationshipEvent.OnOwnerNameChange -> {
                _state.update {
                    it.copy(
                        ownerName = event.ownerName
                    )
                }
            }
            is ACHRelationshipEvent.OnRoutingNumberChange -> {
                _state.update {
                    it.copy(
                        routingNumber = event.routingNumber
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