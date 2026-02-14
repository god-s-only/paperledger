package com.paperledger.app.presentation.ui.features.trade

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paperledger.app.core.UIEvent
import com.paperledger.app.core.Routes
import com.paperledger.app.core.mapErrorMessage
import com.paperledger.app.domain.models.trade.Position
import com.paperledger.app.domain.usecase.auth.GetAccountInfoUseCase
import com.paperledger.app.domain.usecase.auth.GetUserIdUseCase
import com.paperledger.app.domain.usecase.trade.CancelAllPendingOrdersUseCase
import com.paperledger.app.domain.usecase.trade.CloseAllPositionsUseCase
import com.paperledger.app.domain.usecase.trade.CloseOpenPositionUseCase
import com.paperledger.app.domain.usecase.trade.ClosePendingOrderUseCase
import com.paperledger.app.domain.usecase.trade.GetOpenPositionsUseCase
import com.paperledger.app.domain.usecase.trade.GetPendingOrdersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TradeViewModel @Inject constructor(
    private val getOpenPositionsUseCase: GetOpenPositionsUseCase,
    private val getPendingOrdersUseCase: GetPendingOrdersUseCase,
    private val getAccountInfoUseCase: GetAccountInfoUseCase,
    private val getUserIdUseCase: GetUserIdUseCase,
    private val closeOpenPositionUseCase: CloseOpenPositionUseCase,
    private val closePendingOrdersUseCase: ClosePendingOrderUseCase,
    private val closeAllPositionsUseCase: CloseAllPositionsUseCase,
    private val cancelAllPendingOrdersUseCase: CancelAllPendingOrdersUseCase
): ViewModel() {
    private val _state = MutableStateFlow(TradeScreenState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            val accountId = getUserIdUseCase()
            if (accountId != null) {
                startDataStreams(accountId)
            }
        }
    }

    private fun startDataStreams(accountId: String) {
        viewModelScope.launch {
            getAccountInfoUseCase.invoke(accountId).fold(
                onSuccess = { accountInfo ->
                    _state.update {
                        it.copy(
                            equity = accountInfo.lastEquity
                        )
                    }
                },
                onFailure = {

                }
            )
        }
        viewModelScope.launch {
            getOpenPositionsUseCase.invoke(accountId).collectLatest { result ->
                result.fold(
                    onSuccess = { positions ->
                        val positionItems = positions.map { position ->
                            PositionItem(
                                id = position.id,
                                symbol = position.symbol,
                                type = position.side.uppercase(),
                                volume = position.quantity,
                                entryPrice = position.entryPrice,
                                currentPrice = position.currentPrice,
                                pnl = position.unrealizedPl,
                                pnlPercent = position.unrealizedPlPercent,
                                qty = position.quantity
                            )
                        }

                        val totalPnl = positionItems.sumOf { it.pnl }

                        _state.update {
                            it.copy(
                                positions = positionItems,
                                pnl = totalPnl,
                                balance = totalPnl + it.equity
                            )
                        }
                    },
                    onFailure = {

                    }
                )
            }
        }
        viewModelScope.launch {
            loadPendingOrders(accountId)
        }
    }

    private fun closePendingOrder(orderId: String){
        viewModelScope.launch {
            closePendingOrdersUseCase.invoke(orderId, getUserIdUseCase.invoke() ?: "").fold(
                onSuccess = {
                    sendUIEvent(UIEvent.ShowSnackBar(message = "Successfully closed pending order"))
                },
                onFailure = {
                    sendUIEvent(UIEvent.ShowSnackBar(message = mapErrorMessage(it)))
                }
            )
        }
    }

    private suspend fun loadPendingOrders(accountId: String) {
        _state.update { it.copy(isLoading = true) }

        getPendingOrdersUseCase.invoke(accountId).fold(
            onSuccess = { orders ->
                _state.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        pendingOrders = orders.map { order ->
                            OrderItem(
                                id = order.id,
                                symbol = order.symbol,
                                type = when (order.type) {
                                    "market" -> "MARKET"
                                    "limit" -> "LIMIT"
                                    "stop" -> "STOP"
                                    "stop_limit" -> "STOP LIMIT"
                                    "trailing_stop" -> "TRAILING STOP"
                                    else -> order.type.uppercase()
                                },
                                volume = order.quantity,
                                price = order.limitPrice ?: 0.0,
                                quantity = order.quantity,
                                placementTime = order.createdAt,
                                side = order.side
                            )
                        }
                    )
                }
            },
            onFailure = { error ->
                _state.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        error = mapErrorMessage(error)
                    )
                }
                sendUIEvent(UIEvent.ShowSnackBar(message = _state.value.error ?: ""))
            }
        )
    }

    fun onEvent(event: TradeScreenEvent) {
        when(event) {
            TradeScreenEvent.OnRefresh -> {
                viewModelScope.launch {
                    val accountId = getUserIdUseCase()
                    if (accountId != null) {
                        loadPendingOrders(accountId)
                    }
                }
            }
            TradeScreenEvent.OnPlaceTradeClick -> {
                sendUIEvent(UIEvent.Navigate(Routes.PLACE_TRADE_SCREEN))
            }
            is TradeScreenEvent.OnCloseOpenPositionClick -> {
                viewModelScope.launch {
                    closeOpenPositionUseCase.invoke(accountId = getUserIdUseCase() ?: "", symbolOrAssetId = event.symbolOrAssetId).fold(
                        onSuccess = {
                            sendUIEvent(UIEvent.ShowSnackBar(message = "Successfully closed worth of open position"))
                        },
                        onFailure = { e ->
                            sendUIEvent(UIEvent.ShowSnackBar(message = mapErrorMessage(e)))
                        }
                    )
                }
            }
            is TradeScreenEvent.OnPositionClick -> {

            }
            is TradeScreenEvent.OnQuantityChange -> {
                _state.update {
                    it.copy(qty = event.qty)
                }
            }
            is TradeScreenEvent.OnClosePendingOrder -> {
                closePendingOrder(event.orderId)
            }
            is TradeScreenEvent.OnCloseAllPositionsClick -> {
                closeAllPositions()
            }
            is TradeScreenEvent.OnCancelAllPendingOrdersClick -> {
                cancelAllPendingOrders()
            }
        }
    }

    private fun closeAllPositions() {
        viewModelScope.launch {
            closeAllPositionsUseCase.invoke(getUserIdUseCase.invoke() ?: "").fold(
                onSuccess = {
                    sendUIEvent(UIEvent.ShowSnackBar(message = "Successfully closed all positions"))
                },
                onFailure = { e ->
                    sendUIEvent(UIEvent.ShowSnackBar(message = mapErrorMessage(e)))
                }
            )
        }
    }

    private fun cancelAllPendingOrders() {
        viewModelScope.launch {
            cancelAllPendingOrdersUseCase.invoke(getUserIdUseCase.invoke() ?: "").fold(
                onSuccess = {
                    sendUIEvent(UIEvent.ShowSnackBar(message = "Successfully cancelled all pending orders"))
                },
                onFailure = { e ->
                    sendUIEvent(UIEvent.ShowSnackBar(message = mapErrorMessage(e)))
                }
            )
        }
    }

    private fun sendUIEvent(event: UIEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}