package com.paperledger.app.presentation.ui.features.ach_funding

import com.paperledger.app.domain.models.ach.ACHRelationship
import com.paperledger.app.domain.models.funding.Transfer

data class ACHFundingScreenState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val achRelationships: List<ACHRelationship> = emptyList(),
    val transfers: List<Transfer> = emptyList()
)
