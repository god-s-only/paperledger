package com.paperledger.app.presentation.ui.features.watchlists

import com.paperledger.app.data.local.WatchlistsEntity

data class WatchlistsScreenState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val watchlists: List<WatchlistsEntity> = emptyList(),
    val message: String = ""
)
