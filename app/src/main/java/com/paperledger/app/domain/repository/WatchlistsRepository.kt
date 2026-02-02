package com.paperledger.app.domain.repository

import com.paperledger.app.data.local.WatchlistsEntity
import kotlinx.coroutines.flow.Flow

interface WatchlistsRepository {
    fun getWatchlists(accountId: String): Flow<Result<List<WatchlistsEntity>>>
}