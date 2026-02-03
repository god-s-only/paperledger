package com.paperledger.app.presentation.ui.features.trade

sealed class TradeScreenEvent {
    data object OnRefresh: TradeScreenEvent()
    data object OnPlaceTradeClick: TradeScreenEvent()
}