package com.paperledger.app.domain.usecase.assets

import com.paperledger.app.domain.models.assets.AssetsModel
import com.paperledger.app.domain.repository.AssetsRepository
import javax.inject.Inject

class GetAllAssetsUseCase @Inject constructor(private val repository: AssetsRepository) {
    suspend operator fun invoke(): Result<List<AssetsModel>>{
        return repository.getAllAssets()
    }
}