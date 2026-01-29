package com.paperledger.app.presentation.ui.features.funding

sealed class FundingScreenEvent {
    data class OnAmountChange(val amount: String): FundingScreenEvent()
    data object OnSubmit: FundingScreenEvent()
}