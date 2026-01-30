package com.paperledger.app.data.repository

import com.paperledger.app.data.remote.api.AlpacaApiService
import com.paperledger.app.domain.models.assets.AssetsModel
import com.paperledger.app.domain.repository.AssetsRepository
import javax.inject.Inject

class AssetsRepositoryImpl @Inject constructor(private val alpacaApiService: AlpacaApiService): AssetsRepository {
    override suspend fun getAllAssets(): Result<AssetsModel> {
        TODO("Not yet implemented")
    }
}