package com.paperledger.app.data.repository

import android.util.Log
import com.google.gson.Gson
import com.paperledger.app.core.AppError
import com.paperledger.app.core.POLL_INTERVAL_MS
import com.paperledger.app.core.mapError
import com.paperledger.app.data.local.PaperLedgerSession
import com.paperledger.app.data.mappers.account.toAccountInfo
import com.paperledger.app.data.remote.api.AlpacaApiService
import com.paperledger.app.data.remote.dto.account.request.AccountRequestDTO
import com.paperledger.app.data.remote.dto.error.ErrorResponseDTO
import com.paperledger.app.domain.models.trade.AccountInfo
import com.paperledger.app.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import okio.IOException
import org.json.JSONObject
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
                    val errorBody = Gson().fromJson(response.errorBody()?.string(),
                        ErrorResponseDTO::class.java)
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
     @OptIn(ExperimentalCoroutinesApi::class)
        override fun getAccountInfo(accountId: String): Flow<Result<AccountInfo>> {
            return flow {
                while (true) {
                    try {
                        val response = alpacaApi.getAccountById(accountId)

                        if (!response.isSuccessful) {
                            val errorMessage = response.errorBody()?.string()?.let {
                                try {
                                    JSONObject(it).getString("message")
                                } catch (e: Exception) {
                                    "Error fetching account info"
                                }
                            } ?: "Error fetching account info"
                            emit(Result.failure(AppError.HttpError(response.code(), errorMessage)))
                        } else {
                            val body = response.body() ?: run {
                                emit(Result.failure(AppError.EmptyBody))
                                return@flow
                            }
                            val accountInfo = body.toAccountInfo()
                            Log.d("Balance", "${accountInfo.lastEquity}")
                            emit(Result.success(accountInfo))
                        }

                        delay(POLL_INTERVAL_MS)
                    } catch (e: Exception) {
                        emit(Result.failure(mapError(e)))
                        delay(POLL_INTERVAL_MS)
                    }
                }
            }
                .retry(3) { cause -> cause is IOException }
                .catch { e ->
                    e.printStackTrace()
                    emit(Result.failure(mapError(e)))
                }
                .flowOn(Dispatchers.IO)
                .distinctUntilChanged()
        }
    }
