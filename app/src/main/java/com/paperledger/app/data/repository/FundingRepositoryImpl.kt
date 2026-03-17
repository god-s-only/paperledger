package com.paperledger.app.data.repository

import com.google.gson.Gson
import com.paperledger.app.core.AppError
import com.paperledger.app.core.mapError
import com.paperledger.app.data.local.PaperLedgerSession
import com.paperledger.app.data.remote.api.AlpacaApiService
import com.paperledger.app.data.remote.dto.error.ErrorResponseDTO
import com.paperledger.app.data.remote.dto.funding.request.FundingRequestDTO
import com.paperledger.app.domain.repository.FundingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FundingRepositoryImpl @Inject constructor(private val alpacaApiService: AlpacaApiService, private val paperLedgerSession: PaperLedgerSession): FundingRepository {
    override suspend fun requestFunding(request: FundingRequestDTO, accountId: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val res = alpacaApiService.requestTransfer(accountId, request)
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

    override suspend fun storeFundingToken(token: String): Result<Unit> {
        try {
            paperLedgerSession.storeFundingToken(token)
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun getFundingToken(): String? {
        return paperLedgerSession.getFundingToken()
    }
}