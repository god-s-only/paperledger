package com.paperledger.app.presentation.ui.features.chart

sealed class FullTradeChartEvent {
    data class OnQtyChange(val qty: String): FullTradeChartEvent()
    data class OnSymbolChange(val symbol: String): FullTradeChartEvent()
    data class OnTradeClick(val side: String): FullTradeChartEvent()

}