package com.paperledger.app.domain.usecase.watchlists

import com.paperledger.app.domain.repository.WatchlistsRepository
import javax.inject.Inject

class RemoveWatchlistUseCase @Inject constructor(private val repository: WatchlistsRepository) {
    suspend operator fun invoke(accountId: String, watchlistId: String) = repository.removeWatchlist(accountId, watchlistId)
}