package com.paperledger.app.domain.usecase.watchlists

import com.paperledger.app.domain.repository.WatchlistsRepository
import javax.inject.Inject

class CreateWatchlistUseCase @Inject constructor(private val repository: WatchlistsRepository) {
    suspend operator fun invoke(accountId: String, name: String, symbols: List<String>): Result<Unit> {
        return repository.createWatchlist(accountId, name, symbols)
    }
}