package com.paperledger.app.data.repository

import com.google.gson.Gson
import com.paperledger.app.core.AppError
import com.paperledger.app.core.POLL_INTERVAL_MS
import com.paperledger.app.core.mapError
import com.paperledger.app.data.mappers.trade.toDomain
import com.paperledger.app.data.remote.api.AlpacaApiService
import com.paperledger.app.data.remote.dto.error.ErrorResponseDTO
import com.paperledger.app.domain.models.trade.AccountInfo
import com.paperledger.app.domain.models.trade.Order
import com.paperledger.app.domain.models.trade.Position
import com.paperledger.app.domain.repository.TradesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import org.json.JSONObject
import okio.IOException
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class TradesRepositoryImpl @Inject constructor(private val alpacaApiService: AlpacaApiService) : TradesRepository {

    override suspend fun getPendingOrders(accountId: String): Result<List<Order>> {
        return try {
            val response = alpacaApiService.getPendingOrders(accountId)

            if (!response.isSuccessful) {
                val errorBody = response.errorBody()?.string()
                val errorMessage = if (errorBody != null) {
                    try {
                        JSONObject(errorBody).getString("message")
                    } catch (e: Exception) {
                        "Failed to fetch pending orders"
                    }
                } else {
                    "Failed to fetch pending orders"
                }
                return Result.failure(AppError.HttpError(response.code(), errorMessage))
            }

            val body = response.body() ?: return Result.failure(AppError.EmptyBody)
            val orders = body.map { it.toDomain() }

            Result.success(orders)
        } catch (e: Exception) {
            Result.failure(mapError(e))
        }
    }

    // Flow with 5-second polling for open positions
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getOpenPositions(accountId: String): Flow<Result<List<Position>>> {
        return flow {
            while (true) {
                try {
                    val response = alpacaApiService.getOpenPositions(accountId)

                    if (!response.isSuccessful) {
                        val errorMessage = response.errorBody()?.string()?.let {
                            try {
                                JSONObject(it).getString("message")
                            } catch (e: Exception) {
                                "Error fetching positions"
                            }
                        } ?: "Error fetching positions"
                        emit(Result.failure(AppError.HttpError(response.code(), errorMessage)))
                    } else {
                        val body = response.body() ?: run {
                            emit(Result.failure(AppError.EmptyBody))
                            return@flow
                        }
                        val positions = body.map { it.toDomain() }
                        emit(Result.success(positions))
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