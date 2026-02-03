package com.paperledger.app.presentation.ui.features.trade

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paperledger.app.core.UIEvent
import com.paperledger.app.core.Routes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TradeViewModel @Inject constructor(): ViewModel() {
    private val _state = MutableStateFlow(TradeScreenState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        // Initialize with sample data for demo purposes
        viewModelScope.launch {
            loadSampleData()
        }
    }

    private fun loadSampleData() {
        _state.update { it.copy(
            isLoading = false,
            balance = 100000.0,
            freeMargin = 95000.0,
            equity = 100250.0,
            margin = 5000.0,
            pnl = 250.0,
            positions = listOf(
                PositionItem(
                    symbol = "AAPL",
                    type = "BUY",
                    volume = 10.0,
                    entryPrice = 175.50,
                    currentPrice = 178.25,
                    pnl = 275.0,
                    pnlPercent = 1.57,
                    openTime = "2024-01-15 10:30"
                ),
                PositionItem(
                    symbol = "GOOGL",
                    type = "BUY",
                    volume = 5.0,
                    entryPrice = 142.30,
                    currentPrice = 141.50,
                    pnl = -40.0,
                    pnlPercent = -0.56,
                    openTime = "2024-01-14 14:20"
                )
            ),
            pendingOrders = listOf(
                OrderItem(
                    symbol = "TSLA",
                    type = "BUY LIMIT",
                    volume = 15.0,
                    price = 215.00,
                    stopLoss = 210.00,
                    takeProfit = 225.00,
                    placementTime = "2024-01-15 09:15"
                )
            )
        )}
    }

    fun onEvent(event: TradeScreenEvent) {
        when(event) {
            TradeScreenEvent.OnRefresh -> {
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true) }
                    // In real app, refresh data from API
                    loadSampleData()
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