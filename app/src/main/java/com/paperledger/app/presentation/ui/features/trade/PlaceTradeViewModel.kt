package com.paperledger.app.presentation.ui.features.trade

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.paperledger.app.core.UIEvent
import com.paperledger.app.domain.usecase.trade.CreatePositionOrderUseCase
import com.paperledger.app.domain.usecase.trade.PlaceTradeEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow

@HiltViewModel
class PlaceTradeViewModel @Inject constructor(savedStateHandle: SavedStateHandle, private val createPositionOrderUseCase: CreatePositionOrderUseCase): ViewModel() {
    private val _state = MutableStateFlow(PlaceTradeState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        savedStateHandle.get<String>("watchlistName")?.let {
            _state.value = _state.value.copy(symbol = it)
        }
    }

    fun onEvent(event: PlaceTradeEvent){
        when(event){
            is PlaceTradeEvent.OnPlaceTradeButtonClick -> {

            }
        }
    }
}