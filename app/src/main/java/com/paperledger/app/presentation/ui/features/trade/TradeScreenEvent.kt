package com.paperledger.app.presentation.ui.features.trade

sealed class TradeScreenEvent {
    data object OnRefresh : TradeScreenEvent()
    data object OnPlaceTradeClick : TradeScreenEvent()
    data class OnPositionClick(val position: PositionItem) : TradeScreenEvent()
    data class OnCloseOpenPositionClick(val qty: Double, val symbolOrAssetId: String): TradeScreenEvent()
    data class OnQuantityChange(val qty: Double) : TradeScreenEvent()
    data class OnClosePendingOrder(val symbolOrAssetId: String): TradeScreenEvent()
}