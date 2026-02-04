package com.paperledger.app.presentation.ui.features.trade

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paperledger.app.core.UIEvent
import com.paperledger.app.core.Routes
import com.paperledger.app.core.mapErrorMessage
import com.paperledger.app.domain.models.trade.Position
import com.paperledger.app.domain.usecase.auth.GetAccountInfoUseCase
import com.paperledger.app.domain.usecase.auth.GetUserIdUseCase
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
    private val getUserIdUseCase: GetUserIdUseCase
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
            combine(
                getAccountInfoUseCase.invoke(accountId),
                getOpenPositionsUseCase.invoke(accountId)
            ) { accountInfoResult, openPositionsResult ->

                val accountInfoError = accountInfoResult.exceptionOrNull()
                val openPositionsError = openPositionsResult.exceptionOrNull()

                _state.update { current ->

                    var newState = current.copy(
                        isLoading = false,
                        error = null
                    )

                    accountInfoResult.getOrNull()?.let { accountInfo ->
                        newState = newState.copy(
                            balance = accountInfo.lastEquity,
                            equity = accountInfo.lastEquity
                        )
                    }

                    openPositionsResult.getOrNull()?.let { positions ->
                        newState = newState.copy(
                            positions = positions.map { position ->
                                PositionItem(
                                    symbol = position.symbol,
                                    type = position.side.uppercase(),
                                    volume = position.quantity,
                                    entryPrice = position.entryPrice,
                                    currentPrice = position.currentPrice,
                                    pnl = position.unrealizedPl,
                                    pnlPercent = position.unrealizedPlPercent
                                )
                            }
                        )
                    }

                    val error = accountInfoError ?: openPositionsError
                    if (error != null) {
                        newState = newState.copy(
                            error = mapErrorMessage(error)
                        )
                    }

                    newState
                }
            }
        }
        viewModelScope.launch {
            loadPendingOrders(accountId)
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
                                price = order.limitPrice ?: order.stopPrice ?: 0.0,
                                stopLoss = order.stopPrice,
                                takeProfit = order.filledAvgPrice,
                                placementTime = order.createdAt
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
        }
    }

    private fun sendUIEvent(event: UIEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}