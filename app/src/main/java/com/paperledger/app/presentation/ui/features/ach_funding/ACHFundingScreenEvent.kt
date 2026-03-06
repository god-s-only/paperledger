package com.paperledger.app.presentation.ui.features.ach_funding

sealed class ACHFundingScreenEvent {
    object OnRefresh : ACHFundingScreenEvent()
    object OnNavigateBack : ACHFundingScreenEvent()
}
