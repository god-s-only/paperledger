package com.paperledger.app.presentation.ui.features.watchlists

import com.paperledger.app.data.local.WatchlistsEntity

sealed class WatchlistsAction {
    data object OnAddWatchlistClick : WatchlistsAction()
    data class OnWatchlistClick(val watchlist: WatchlistsEntity) : WatchlistsAction()
}