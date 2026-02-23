package com.paperledger.app.presentation.ui.features.chart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paperledger.app.core.UIEvent
import com.paperledger.app.core.mapErrorMessage
import com.paperledger.app.data.remote.dto.position_order_post.PositionRequestDTO
import com.paperledger.app.domain.usecase.auth.GetUserIdUseCase
import com.paperledger.app.domain.usecase.trade.CreatePositionOrderUseCase
import jakarta.inject.Inject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class FullTradeChartViewModel @Inject constructor(
    private val createPositionOrderUseCase: CreatePositionOrderUseCase,
    private val getUserIdUseCase: GetUserIdUseCase
): ViewModel() {
    private val _state = MutableStateFlow(FullTradeChartState())
    val state = _state.asStateFlow()
    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: FullTradeChartEvent){
        when(event){
            is FullTradeChartEvent.OnTradeClick -> TODO()
            is FullTradeChartEvent.OnSymbolChange -> _state.update { it.copy(symbol = event.symbol) }
            is FullTradeChartEvent.OnQtyChange -> _state.update { it.copy(qty = event.qty) }
        }
    }

    private fun createPositionOrder(){
        viewModelScope.launch {
            val res = createPositionOrderUseCase.invoke(getUserIdUseCase() ?: "",
                PositionRequestDTO(
                    _state.value.qty, _state.value.side,
                    _state.value.symbol,
                    "day",
                    "market",
                    null,
                    null)
            )
            when{
                res.isSuccess -> {
                    sendUIEvent(UIEvent.ShowSnackBar(message = "Position Order Created"))
                }
                res.isFailure -> {
                    _state.update {
                        it.copy(
                            error = mapErrorMessage(res.exceptionOrNull()!!)
                        )
                    }
                    sendUIEvent(UIEvent.ShowSnackBar(message = _state.value.error ?: ""))
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