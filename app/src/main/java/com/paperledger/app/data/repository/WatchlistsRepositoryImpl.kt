package com.paperledger.app.data.repository

import com.paperledger.app.data.remote.api.AlpacaApiService
import com.paperledger.app.data.remote.dto.watchlists_get.GetWatchlistsDTO
import com.paperledger.app.domain.repository.WatchlistsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WatchlistsRepositoryImpl @Inject constructor(private val alpacaApiService: AlpacaApiService): WatchlistsRepository {
    override suspend fun getWatchlists(accountId: String): Flow<Result<Unit>> {
        TODO("Not yet implemented")
    }
}