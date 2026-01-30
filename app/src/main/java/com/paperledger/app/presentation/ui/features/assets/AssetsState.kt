package com.paperledger.app.presentation.ui.features.assets

import com.paperledger.app.domain.models.assets.AssetsModel

data class AssetsState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val assets: List<AssetsModel> = emptyList()
)
