package com.paperledger.app.presentation.ui.features.chart

import com.paperledger.app.data.local.WatchlistsEntity

data class FullTradeChartState(
    val error: String? = null,
    val qty: String = "0.01",
    val symbol: String = "",
    val side: String = "",
    val isDarkMode: Boolean = true,
    val showQuickTrade: Boolean = false,
    val watchlists: List<WatchlistsEntity> = emptyList()
)
