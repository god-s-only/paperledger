package com.paperledger.app.data.repository

import com.google.gson.Gson
import com.paperledger.app.core.AppError
import com.paperledger.app.core.mapError
import com.paperledger.app.data.mappers.assets.toAssetsDomain
import com.paperledger.app.data.remote.api.AlpacaApiService
import com.paperledger.app.data.remote.dto.error.ErrorResponseDTO
import com.paperledger.app.domain.models.assets.AssetsModel
import com.paperledger.app.domain.repository.AssetsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AssetsRepositoryImpl @Inject constructor(private val alpacaApiService: AlpacaApiService): AssetsRepository {
    override suspend fun getAllAssets(): Result<List<AssetsModel>> {
        return withContext(Dispatchers.IO){
            try {
                val res = alpacaApiService.getAllAssets()
                if(res.isSuccessful){
                    val body = res.body() ?: return@withContext Result.failure(AppError.EmptyBody)
                    Result.success(body.map { it.toAssetsDomain() })
                }else{
                    val errorBody = Gson().fromJson(res.errorBody()?.string(), ErrorResponseDTO::class.java)
                    Result.failure(AppError.HttpError(res.code(), errorBody.message))
                }
            }catch (e: Exception){
                Result.failure(mapError(e))
            }
        }
    }
}