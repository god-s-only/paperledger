package com.paperledger.app.data.repository

import com.google.gson.Gson
import com.paperledger.app.core.AppError
import com.paperledger.app.core.mapError
import com.paperledger.app.data.local.PaperledgerDAO
import com.paperledger.app.domain.models.watchlists.WatchlistsEntity
import com.paperledger.app.data.mappers.watchlists.toDomain
import com.paperledger.app.data.remote.api.AlpacaApiService
import com.paperledger.app.data.remote.dto.error.ErrorResponseDTO
import com.paperledger.app.data.remote.dto.watchlists_get.GetWatchlistsDTO
import com.paperledger.app.domain.repository.WatchlistsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.retry
import javax.inject.Inject
import kotlin.run

class WatchlistsRepositoryImpl @Inject constructor(private val alpacaApiService: AlpacaApiService, private val dao: PaperledgerDAO): WatchlistsRepository {
    override suspend fun getWatchlists(accountId: String): Flow<Result<List<WatchlistsEntity>>> {
        val localWatchlists = dao.observeAllWatchlists()
            .mapLatest { entities ->
                if(entities.isNullOrEmpty()){
                    Result.failure(AppError.EmptyBody)
                }else{
                    Result.success(entities)
                }
            }
        val remoteRefresh = flow {
            val res = alpacaApiService.getWatchlists(accountId)
            if(!res.isSuccessful){
                emit(Result.failure(AppError.HttpError(res.code(), Gson().fromJson(res.errorBody()?.string(),
                    ErrorResponseDTO::class.java).message)))
                return@flow
            }
            val body = res.body() ?: run {
                emit(Result.failure(AppError.EmptyBody))
                return@flow
            }
            dao.replaceAllWatchlists(body.map { it.toDomain() })
            emit(Result.success(body.map { it.toDomain() }))
        }
            .retry(3) {
                cause ->  cause is AppError.NetworkUnavailable
            }
            .catch { e ->
                emit(Result.failure(mapError(e)))
            }
            .flowOn(Dispatchers.IO)

        return merge(
            localWatchlists,
            remoteRefresh
        )
            .flowOn(Dispatchers.IO)
            .distinctUntilChanged()
    }
}