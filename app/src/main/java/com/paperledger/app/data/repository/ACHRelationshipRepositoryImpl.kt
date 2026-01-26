package com.paperledger.app.data.repository

import android.app.Service
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
                    val body =
                        res.body() ?: return@withContext Result.failure(Exception("Empty body"))
                    Result.success(Unit)
                } else {
                    Result.failure(Exception("Failed to create ACH relationship"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }

        }
    }
}