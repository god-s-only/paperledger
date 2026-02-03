package com.paperledger.app.presentation.ui.features.trade

data class TradeScreenState(
    val isLoading: Boolean = false,
    val balance: Double = 0.0,
    val freeMargin: Double = 0.0,
    val equity: Double = 0.0,
    val margin: Double = 0.0,
    val pnl: Double = 0.0,
    val positions: List<PositionItem> = emptyList(),
    val pendingOrders: List<OrderItem> = emptyList()
)

data class PositionItem(
    val symbol: String,
    val type: String, // "BUY" or "SELL"
    val volume: Double,
    val entryPrice: Double,
    val currentPrice: Double,
    val pnl: Double,
    val pnlPercent: Double,
    val openTime: String
)

data class OrderItem(
    val symbol: String,
    val type: String, // "BUY LIMIT", "SELL LIMIT", "BUY STOP", "SELL STOP"
    val volume: Double,
    val price: Double,
    val stopLoss: Double? = null,
    val takeProfit: Double? = null,
    val placementTime: String
)