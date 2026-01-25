package com.paperledger.app.data.repository

import com.google.gson.Gson
import com.paperledger.app.core.AppError
import com.paperledger.app.data.local.PaperLedgerSession
import com.paperledger.app.data.remote.api.AlpacaApiService
import com.paperledger.app.data.remote.dto.account.request.AccountRequestDTO
import com.paperledger.app.data.remote.dto.account.response.error.AccountResponseErrorDTO
import com.paperledger.app.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val alpacaApi: AlpacaApiService, private val paperLedgerSession: PaperLedgerSession): AuthRepository {
    override suspend fun createAccount(accountRequest: AccountRequestDTO): Result<String> {
        return withContext(Dispatchers.IO){
            try {
                val response = alpacaApi.createAccount(accountRequest)
                if(response.isSuccessful){
                    val body = response.body() ?: return@withContext Result.failure(AppError.EmptyBody)
                    Result.success(body.id)
                }else{
                    val errorBody = Gson().fromJson(response.errorBody()?.string(), AccountResponseErrorDTO::class.java)
                    Result.failure(AppError.HttpError(response.code(), errorBody.message))
                }
            }catch (e: Exception){
                Result.failure(mapError(e))
            }
        }
    }

    override suspend fun updateAccount(
        accountId: String,
        accountRequest: AccountRequestDTO
    ): Result<Unit> {
        return withContext(Dispatchers.IO){
            try {
                val res = alpacaApi.updateAccount(accountId, accountRequest)
                if(res.isSuccessful){
                    val body = res.body() ?: return@withContext Result.failure(AppError.EmptyBody)
                    Result.success(Unit)
                }else{
                    Result.failure(AppError.HttpError(res.code(), res.message()))
                }
            }catch (e: Exception){
                Result.failure(mapError(e))
            }
        }
    }

    override suspend fun storeUserId(userId: String): Result<Unit> {
        return try {
            paperLedgerSession.storeUserId(userId)
            Result.success(Unit)
        }catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun getUserId(): String? {
        return paperLedgerSession.getUserId()
    }

    private fun mapError(e: Exception): AppError{
        return when(e){
            is IOException -> AppError.NetworkUnavailable
            is HttpException -> AppError.HttpError(e.code(), e.message)
            else -> AppError.Unknown(e.message)
        }
    }
}