package com.paperledger.app.presentation.ui.features.watchlists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paperledger.app.core.UIEvent
import com.paperledger.app.core.mapErrorMessage
import com.paperledger.app.domain.usecase.auth.GetUserIdUseCase
import com.paperledger.app.domain.usecase.watchlists.GetWatchlistsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchlistsScreenViewModel @Inject constructor(private val getWatchlistsUseCase: GetWatchlistsUseCase, private val getUserIdUseCase: GetUserIdUseCase): ViewModel() {
    private val _state = MutableStateFlow(WatchlistsScreenState())
    val state = _state.onStart {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    accountId = getUserIdUseCase.invoke() ?: "",
                    isLoading = true
                )
            }
        }
        getWatchlists()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = WatchlistsScreenState()
    )

    fun getWatchlists(){
            _state.value = _state.value.copy(isLoading = true)
            getWatchlistsUseCase.invoke(_state.value.accountId).onEach { result ->
                result.fold(
                    onSuccess = { entities ->
                        if(entities.isEmpty()){
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    message = "No watchlists yet. Add some!",
                                    watchlists = emptyList()
                                )
                            }
                        }else{
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    error = null,
                                    watchlists = entities
                                )
                            }
                        }
                    },
                    onFailure = { exception ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = mapErrorMessage(exception)
                            )
                        }
                    }
                )
            }.launchIn(viewModelScope)
        }

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    private fun sendUIEvent(event: UIEvent){
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}