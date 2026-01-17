package com.paperledger.app.data.repository

import com.paperledger.app.core.AppError
import com.paperledger.app.data.remote.api.AlpacaApiService
import com.paperledger.app.data.remote.dto.account.request.AccountRequestDTO
import com.paperledger.app.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.IOException
import retrofit2.HttpException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val alpacaApi: AlpacaApiService): AuthRepository {
    override suspend fun createAccount(accountRequest: AccountRequestDTO): Result<String> {
        return withContext(Dispatchers.IO){
            try {
                val response = alpacaApi.createAccount(accountRequest)
                if(response.isSuccessful){
                    val body = response.body() ?: return@withContext Result.failure(AppError.EmptyBody)
                    Result.success(body.id)
                }else{
                    Result.failure(AppError.HttpError(response.code()))
                }
            }catch (e: Exception){
                Result.failure(mapError(e))
            }
        }
    }
    private fun mapError(e: Exception): AppError{
        return when(e){
            is IOException -> AppError.NetworkUnavailable
            is HttpException -> AppError.HttpError(e.code())
            else -> AppError.Unknown(e.message)
        }
    }
}