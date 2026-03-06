package com.paperledger.app.presentation.ui.features.ach_funding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paperledger.app.core.UIEvent
import com.paperledger.app.core.mapErrorMessage
import com.paperledger.app.domain.usecase.ach.GetACHRelationshipsUseCase
import com.paperledger.app.domain.usecase.auth.GetUserIdUseCase
import com.paperledger.app.domain.usecase.funding.GetTransfersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ACHFundingViewModel @Inject constructor(
    private val getACHRelationshipsUseCase: GetACHRelationshipsUseCase,
    private val getTransfersUseCase: GetTransfersUseCase,
    private val getUserIdUseCase: GetUserIdUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ACHFundingScreenState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }

            val accountId = getUserIdUseCase() ?: ""
            if (accountId.isBlank()) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = "Account ID not found"
                    )
                }
                return@launch
            }

            // Fetch both ACH relationships and transfers
            val relationshipsResult = getACHRelationshipsUseCase(accountId)
            val transfersResult = getTransfersUseCase(accountId)

            relationshipsResult.fold(
                onSuccess = { relationships ->
                    transfersResult.fold(
                        onSuccess = { transfers ->
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    achRelationships = relationships,
                                    transfers = transfers,
                                    error = null
                                )
                            }
                        },
                        onFailure = { e ->
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    achRelationships = relationships,
                                    error = mapErrorMessage(e)
                                )
                            }
                            sendUIEvent(UIEvent.ShowSnackBar(message = "Transfers: ${mapErrorMessage(e)}"))
                        }
                    )
                },
                onFailure = { e ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = mapErrorMessage(e)
                        )
                    }
                    sendUIEvent(UIEvent.ShowSnackBar(message = mapErrorMessage(e)))
                }
            )
        }
    }

    private fun refreshData() {
        viewModelScope.launch {
            _state.update { it.copy(isRefreshing = true, error = null) }

            val accountId = getUserIdUseCase() ?: ""
            if (accountId.isBlank()) {
                _state.update { it.copy(isRefreshing = false) }
                return@launch
            }

            val relationshipsResult = getACHRelationshipsUseCase(accountId)
            val transfersResult = getTransfersUseCase(accountId)

            relationshipsResult.fold(
                onSuccess = { relationships ->
                    transfersResult.fold(
                        onSuccess = { transfers ->
                            _state.update {
                                it.copy(
                                    isRefreshing = false,
                                    achRelationships = relationships,
                                    transfers = transfers
                                )
                            }
                            sendUIEvent(UIEvent.ShowSnackBar(message = "Data refreshed"))
                        },
                        onFailure = { e ->
                            _state.update {
                                it.copy(
                                    isRefreshing = false,
                                    achRelationships = relationships
                                )
                            }
                            sendUIEvent(UIEvent.ShowSnackBar(message = "Transfers refresh failed: ${mapErrorMessage(e)}"))
                        }
                    )
                },
                onFailure = { e ->
                    _state.update { it.copy(isRefreshing = false) }
                    sendUIEvent(UIEvent.ShowSnackBar(message = "Refresh failed: ${mapErrorMessage(e)}"))
                }
            )
        }
    }

    fun onEvent(event: ACHFundingScreenEvent) {
        when (event) {
            is ACHFundingScreenEvent.OnRefresh -> {
                refreshData()
            }
            is ACHFundingScreenEvent.OnNavigateBack -> {
                viewModelScope.launch {
                    _uiEvent.send(UIEvent.PopBackStack)
                }
            }
        }
    }

    private fun sendUIEvent(event: UIEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}
