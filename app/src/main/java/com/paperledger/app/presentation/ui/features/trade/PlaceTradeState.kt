package com.paperledger.app.presentation.ui.features.trade

import com.paperledger.app.data.local.WatchlistsEntity

data class PlaceTradeState(
    val error: String? = null,
    val isLoading: Boolean = false,
    val symbol: String = "",
    val orderType: String = "market",
    val side: String = "buy",
    val qty: String = "",
    val limitPrice: String = "",
    val timeInForce: String = "gtc",
    val stopLoss: String = "",
    val takeProfit: String = "",
    val watchlists: List<WatchlistsEntity> = emptyList()
)
