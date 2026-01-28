package com.paperledger.app.data.repository

import android.app.Service
import com.paperledger.app.core.AppError
import com.paperledger.app.core.mapError
import com.paperledger.app.data.remote.api.AlpacaApiService
import com.paperledger.app.data.remote.dto.ach.request.ACHRelationshipsRequestDTO
import com.paperledger.app.domain.repository.ACHRelationshipRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ACHRelationshipRepositoryImpl @Inject constructor(private val alpacaAPIService: AlpacaApiService): ACHRelationshipRepository {
    override suspend fun createACHRelationship(
        request: ACHRelationshipsRequestDTO,
        accountId: String
    ): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val res = alpacaAPIService.createACHRelationship(accountId, request)
                if (res.isSuccessful) {
                    val body = res.body() ?: return@withContext Result.failure(AppError.EmptyBody)
                    Result.success(Unit)
                } else {
                    Result.failure(AppError.HttpError(res.code(), res.message()))
                }
            } catch (e: Exception) {
                Result.failure(mapError(e))
            }

        }
    }
}