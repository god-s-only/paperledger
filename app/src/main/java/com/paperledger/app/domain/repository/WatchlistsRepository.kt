package com.paperledger.app.domain.repository

import com.paperledger.app.domain.models.watchlists.WatchlistsEntity
import com.paperledger.app.data.remote.dto.watchlists_get.GetWatchlistsDTO
import kotlinx.coroutines.flow.Flow

interface WatchlistsRepository {
    fun getWatchlists(accountId: String): Flow<Result<List<WatchlistsEntity>>>
}