package com.paperledger.app.data.repository

import com.google.gson.Gson
import com.paperledger.app.core.AppError
import com.paperledger.app.core.mapError
import com.paperledger.app.data.local.PaperLedgerSession
import com.paperledger.app.data.remote.api.AlpacaApiService
import com.paperledger.app.data.remote.dto.ach.request.ACHRelationshipsRequestDTO
import com.paperledger.app.data.remote.dto.error.ErrorResponseDTO
import com.paperledger.app.domain.repository.ACHRelationshipRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ACHRelationshipRepositoryImpl @Inject constructor(private val alpacaAPIService: AlpacaApiService, private val paperLedgerSession: PaperLedgerSession): ACHRelationshipRepository {
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
                    val errorBody = Gson().fromJson(res.errorBody()?.string(), ErrorResponseDTO::class.java)
                    Result.failure(AppError.HttpError(res.code(), errorBody.message))
                }
            } catch (e: Exception) {
                Result.failure(mapError(e))
            }

        }
    }

    override suspend fun storeACHRelationshipToken(token: String): Result<Unit> {
        return try {
            paperLedgerSession.storeACHRelationshipToken(token)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getACHRelationshipId(accountId: String): Result<String> {
        return withContext(Dispatchers.IO){
            try {
                val res = alpacaAPIService.getACHRelationship(accountId)
                if(res.isSuccessful){
                    val body = res.body() ?: return@withContext Result.failure(AppError.EmptyBody)
                    Result.success(body.first().id)
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