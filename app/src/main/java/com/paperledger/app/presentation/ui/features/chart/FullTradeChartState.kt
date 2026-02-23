package com.paperledger.app.presentation.ui.features.chart

data class FullTradeChartState(
    val error: String? = null,
    val qty: String = "",
    val symbol: String = "",
    val side: String = "",
    val isDarkMode: Boolean = true,
    val showQuickTrade: Boolean = false
)
