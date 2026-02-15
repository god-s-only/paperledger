package com.paperledger.app.presentation.ui.features.trade

data class PlaceTradeState(
    val error: String? = null,
    val isLoading: Boolean = false,
    val symbol: String = ""
)
