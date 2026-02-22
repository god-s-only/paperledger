package com.paperledger.app.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.gson.Gson
import com.paperledger.app.core.AppError
import com.paperledger.app.core.mapError
import com.paperledger.app.data.mappers.assets.toAssetsDomain
import com.paperledger.app.data.paging.AssetsPagingSource
import com.paperledger.app.data.remote.api.AlpacaApiService
import com.paperledger.app.data.remote.dto.error.ErrorResponseDTO
import com.paperledger.app.domain.models.assets.AssetsModel
import com.paperledger.app.domain.repository.AssetsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AssetsRepositoryImpl @Inject constructor(private val alpacaApiService: AlpacaApiService): AssetsRepository {
    override suspend fun getAllAssets(searchQuery: String): Flow<PagingData<AssetsModel>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                prefetchDistance = PREFETCH_DISTANCE,
                enablePlaceholders = false,
                initialLoadSize = INITIAL_LOAD_SIZE
            ),
            pagingSourceFactory = {
                AssetsPagingSource(
                    alpacaApiService = alpacaApiService,
                    searchQuery = searchQuery
                )
            }
        ).flow
    }
    companion object {
        private const val PAGE_SIZE = 30
        private const val PREFETCH_DISTANCE = 10
        private const val INITIAL_LOAD_SIZE = 60
    }
}