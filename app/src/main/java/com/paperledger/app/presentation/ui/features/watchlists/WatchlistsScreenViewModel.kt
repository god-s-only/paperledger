package com.paperledger.app.presentation.ui.features.watchlists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paperledger.app.core.Routes
import com.paperledger.app.core.UIEvent
import com.paperledger.app.core.mapErrorMessage
import com.paperledger.app.domain.usecase.auth.GetUserIdUseCase
import com.paperledger.app.domain.usecase.watchlists.GetWatchlistsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
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
    val state = _state.asStateFlow()

    init {
        getWatchlists()
    }

    fun getWatchlists(){
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            getWatchlistsUseCase.invoke(getUserIdUseCase() ?: "").collectLatest { result ->
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
                        println("ViewModel received failure: ${exception.javaClass.simpleName} - ${exception.message}")
                        exception.printStackTrace()
                        _state.update {
                            it.copy(
                                isLoading = false,
                                error = mapErrorMessage(exception)
                            )
                        }
                        sendUIEvent(UIEvent.ShowSnackBar(message = _state.value.message))
                    }
                )
            }
        }
    }

    fun onEvent(action: WatchlistsAction){
        when(action){
            is WatchlistsAction.OnAddWatchlistClick -> {
                sendUIEvent(UIEvent.Navigate(Routes.ASSETS_SCREEN))
            }
        }
    }

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    private fun sendUIEvent(event: UIEvent){
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}