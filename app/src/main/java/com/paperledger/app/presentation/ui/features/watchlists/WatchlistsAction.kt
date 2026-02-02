package com.paperledger.app.presentation.ui.features.watchlists

sealed class WatchlistsAction {
    data object OnAddWatchlistClick : WatchlistsAction()
}