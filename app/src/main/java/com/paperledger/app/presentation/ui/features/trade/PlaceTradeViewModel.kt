package com.paperledger.app.presentation.ui.features.trade

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.paperledger.app.core.UIEvent
import com.paperledger.app.domain.usecase.trade.CreatePendingOrderUseCase
import com.paperledger.app.domain.usecase.trade.CreatePositionOrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

@HiltViewModel
class PlaceTradeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val createPositionOrderUseCase: CreatePositionOrderUseCase,
    private val createPendingOrderUseCase: CreatePendingOrderUseCase
): ViewModel() {
    private val _state = MutableStateFlow(PlaceTradeState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        savedStateHandle.get<String>("watchlistName")?.let {
            _state.value = _state.value.copy(symbol = it)
        } ?: _state.update {
            it.copy(symbol = "AAPL")
        }
    }

    fun onEvent(event: PlaceTradeEvent){
        when(event){
            is PlaceTradeEvent.OnLimitPriceChange -> {
                _state.update { it.copy(limitPrice = event.limitPrice) }
            }
            is PlaceTradeEvent.OnOrderTypeChange -> {
                _state.update { it.copy(orderType = event.orderType) }
            }
            is PlaceTradeEvent.OnQtyChange -> {
                _state.update { it.copy(qty = event.qty) }
            }
            is PlaceTradeEvent.OnSideChange -> {
                _state.update { it.copy(side = event.side) }
            }
            is PlaceTradeEvent.OnTimeInForceChange -> {
                _state.update { it.copy(timeInForce = event.timeInForce) }
            }
        }
    }
}