package com.paperledger.app.domain.repository

import androidx.paging.PagingData
import com.paperledger.app.domain.models.assets.AssetsModel
import kotlinx.coroutines.flow.Flow

interface AssetsRepository {
    suspend fun getAllAssets(searchQuery: String): Flow<PagingData<AssetsModel>>
}