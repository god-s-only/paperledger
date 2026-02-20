package com.paperledger.app.presentation.ui.features.trade

sealed class PlaceTradeEvent {
    data class OnOrderTypeChange(val orderType: String): PlaceTradeEvent()
    data class OnSideChange(val side: String): PlaceTradeEvent()
    data class OnQtyChange(val qty: String): PlaceTradeEvent()
    data class OnLimitPriceChange(val limitPrice: String): PlaceTradeEvent()
    data class OnTimeInForceChange(val timeInForce: String): PlaceTradeEvent()
}