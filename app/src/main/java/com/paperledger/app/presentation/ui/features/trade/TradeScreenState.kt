package com.paperledger.app.presentation.ui.features.trade

data class TradeScreenState(
    val isLoading: Boolean = false,
    val balance: Double = 0.0,
    val freeMargin: Double = 0.0,
    val equity: Double = 0.0,
    val margin: Double = 0.0,
    val pnl: Double = 0.0,
    val error: String? = null,
    val positions: List<PositionItem> = emptyList(),
    val pendingOrders: List<OrderItem> = emptyList()
)

data class PositionItem(
    val symbol: String = "",
    val type: String = "",
    val volume: Double = 0.0,
    val entryPrice: Double = 0.0,
    val currentPrice: Double = 0.0,
    val pnl: Double = 0.0,
    val pnlPercent: Double = 0.0,
    val qty: Double = 0.0
)

data class OrderItem(
    val symbol: String = "",
    val type: String = "",
    val quantity: Double = 0.0,
    val volume: Double = 0.0,
    val price: Double = 0.0,
    val stopLoss: Double? = null,
    val takeProfit: Double? = null,
    val placementTime: String = "",
    val side: String = ""
)