package com.paperledger.app.presentation.ui.features.watchlists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paperledger.app.core.UIEvent
import com.paperledger.app.domain.usecase.auth.GetUserIdUseCase
import com.paperledger.app.domain.usecase.watchlists.GetWatchlistsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchlistsScreenViewModel @Inject constructor(private val getWatchlistsUseCase: GetWatchlistsUseCase, private val getUserIdUseCase: GetUserIdUseCase): ViewModel() {
    private val _state = MutableStateFlow(WatchlistsScreenState())
    val state = _state.onStart {

    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = WatchlistsScreenState()
    )



    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()


    private fun sendUIEvent(event: UIEvent){
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}