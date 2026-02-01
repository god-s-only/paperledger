package com.paperledger.app.domain.usecase.watchlists

import com.paperledger.app.domain.repository.WatchlistsRepository
import javax.inject.Inject

class GetWatchlistsUseCase @Inject constructor(private val repository: WatchlistsRepository) {
    suspend operator fun invoke(accountId: String) = repository.getWatchlists(accountId)
}