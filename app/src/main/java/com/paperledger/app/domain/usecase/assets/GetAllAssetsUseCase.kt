package com.paperledger.app.domain.usecase.assets

import androidx.paging.PagingData
import com.paperledger.app.domain.models.assets.AssetsModel
import com.paperledger.app.domain.repository.AssetsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllAssetsUseCase @Inject constructor(private val repository: AssetsRepository) {
    operator fun invoke(searchQuery: String = ""): Flow<PagingData<AssetsModel>> {
        return repository.getAllAssets(searchQuery)
    }
}