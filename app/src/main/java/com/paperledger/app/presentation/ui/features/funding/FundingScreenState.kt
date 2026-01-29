package com.paperledger.app.presentation.ui.features.funding

data class FundingScreenState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val amount: String = "",
    val relationshipId: String = "",
    val transferType: String = "",
    val direction: String = ""
)
