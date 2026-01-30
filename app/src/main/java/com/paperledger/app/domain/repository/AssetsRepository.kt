package com.paperledger.app.domain.repository

import com.paperledger.app.domain.models.assets.AssetsModel

interface AssetsRepository {
    suspend fun getAllAssets(): Result<AssetsModel>
}