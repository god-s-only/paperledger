package com.paperledger.app.presentation.ui.features.assets

import com.paperledger.app.domain.models.assets.AssetsModel

sealed class AssetsScreenEvent {
    data class OnSearchQueryChange(val searchQuery: String): AssetsScreenEvent()
    data class OnAssetClick(val asset: AssetsModel): AssetsScreenEvent()
}