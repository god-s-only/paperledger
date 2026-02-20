package com.paperledger.app.presentation.ui.features.trade

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paperledger.app.core.UIEvent
import com.paperledger.app.core.mapErrorMessage
import com.paperledger.app.data.remote.dto.position_order_post.PositionRequestDTO
import com.paperledger.app.data.remote.dto.position_order_post.StopLossDTO
import com.paperledger.app.data.remote.dto.position_order_post.TakeProfitDTO
import com.paperledger.app.domain.usecase.auth.GetUserIdUseCase
import com.paperledger.app.domain.usecase.trade.CreatePendingOrderUseCase
import com.paperledger.app.domain.usecase.trade.CreatePositionOrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class PlaceTradeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val createPositionOrderUseCase: CreatePositionOrderUseCase,
    private val createPendingOrderUseCase: CreatePendingOrderUseCase,
    private val getUserIdUseCase: GetUserIdUseCase
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

            is PlaceTradeEvent.OnStopLossChange -> {
                _state.update { it.copy(stopLoss = event.stopLoss) }
            }
            is PlaceTradeEvent.OnTakeProfitChange -> {
                _state.update { it.copy(takeProfit = event.takeProfit) }
            }
            is PlaceTradeEvent.OnPlacePositionOrder -> {
                createPositionOrder()
            }
        }
    }

    fun createPositionOrder(){
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }
            createPositionOrderUseCase.invoke(getUserIdUseCase.invoke() ?: "", PositionRequestDTO(_state.value.qty, _state.value.side, _state.value.symbol, _state.value.timeInForce, _state.value.orderType,
                TakeProfitDTO(_state.value.takeProfit),
                StopLossDTO(_state.value.stopLoss, _state.value.stopLoss)
            )).fold(
                onSuccess = {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = null
                        )
                    }
                    sendUIEvent(UIEvent.PopBackStack)
                    sendUIEvent(UIEvent.ShowSnackBar(message = "Order Completed"))
                },
                onFailure = { e ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = mapErrorMessage(e)
                        )
                    }
                    sendUIEvent(UIEvent.ShowSnackBar(message = _state.value.error!!))
                }
            )
        }
    }
    private fun sendUIEvent(event: UIEvent){
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}