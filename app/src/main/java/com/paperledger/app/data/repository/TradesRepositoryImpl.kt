package com.paperledger.app.data.repository

import android.util.Log
import com.google.gson.Gson
import com.paperledger.app.core.AppError
import com.paperledger.app.core.POLL_INTERVAL_MS
import com.paperledger.app.core.mapError
import com.paperledger.app.data.local.PaperledgerDAO
import com.paperledger.app.data.mappers.trade.toDomain
import com.paperledger.app.data.mappers.trade.toEntity
import com.paperledger.app.data.remote.api.AlpacaApiService
import com.paperledger.app.data.remote.dto.error.ErrorResponseDTO
import com.paperledger.app.data.remote.dto.pending_order_post.OrderRequestDTO
import com.paperledger.app.data.remote.dto.position_order_post.PositionRequestDTO
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
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import org.json.JSONObject
import okio.IOException
import javax.inject.Inject
import kotlin.run

class TradesRepositoryImpl @Inject constructor(private val alpacaApiService: AlpacaApiService, private val dao: PaperledgerDAO) : TradesRepository {

    override suspend fun getPendingOrders(accountId: String): Result<List<Order>> {
        return withContext(Dispatchers.IO) {
            try {
                val res = alpacaApiService.getPendingOrders(accountId)
                if (!res.isSuccessful) {
                    val errorBody = Gson().fromJson(res.errorBody()?.string(), ErrorResponseDTO::class.java)
                    return@withContext Result.failure(AppError.HttpError(res.code(), errorBody.message))
                }
                val body = res.body() ?: return@withContext Result.failure(AppError.EmptyBody)
                Result.success(body.map { it.toDomain() })
            } catch (e: Exception) {
                Result.failure(mapError(e))
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getOpenPositions(accountId: String): Flow<Result<List<Position>>> {
        val localPositions = dao.observeAllPositions().mapLatest { entities ->
            Result.success(entities.map { it.toDomain() })
        }
        val remotePositions = flow {
            while (currentCoroutineContext().isActive) {
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
                        dao.replaceAllPositions(body.map { it.toEntity() })
                        val positions = body.map { it.toDomain() }
                        emit(Result.success(positions))
                        Log.d("Positions", "Initialized")
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
        return merge(
            localPositions,
            remotePositions
        )
            .flowOn(Dispatchers.IO)
            .distinctUntilChanged()
    }

    override suspend fun createPendingOrder(accountId: String, orderRequest: OrderRequestDTO): Result<Unit> {
        return withContext(Dispatchers.IO){
            try {
                val res = alpacaApiService.createPendingOrder(accountId, orderRequest)
                if(!res.isSuccessful){
                    val errorMessage = res.errorBody()?.string()?.let {
                        try {
                            JSONObject(it).getString("message")
                        }catch (e: Exception){
                            "Error creating pending order"
                        }
                    } ?: "Error creating pending order"
                    Result.failure(AppError.HttpError(res.code(), errorMessage))
                }else{
                    Result.success(Unit)
                }
            }catch (e: Exception){
                Result.failure(mapError(e))
            }
        }
    }

    override suspend fun createPositionOrder(accountId: String, positionRequest: PositionRequestDTO): Result<Unit> {
        return withContext(Dispatchers.IO){
            try {
                val res = alpacaApiService.createPositionOrder(accountId, positionRequest)
                if(!res.isSuccessful){
                    val errorMessage = res.errorBody()?.string()?.let {
                        try {
                            JSONObject(it).getString("message")
                        }catch (e: Exception){
                            "Error creating position order"
                        }
                    } ?: "Error creating position order"
                    Result.failure(AppError.HttpError(res.code(), errorMessage))
                }else{
                    Result.success(Unit)
                }
            }catch (e: Exception){
                Result.failure(mapError(e))
            }
        }
    }

    override suspend fun closePosition(
        accountId: String,
        symbolOrAssetId: String
    ): Result<Unit> {
        return withContext(Dispatchers.IO){
            try {
                val res = alpacaApiService.closePosition(accountId, symbolOrAssetId)
                if(!res.isSuccessful){
                    val errorMessage = res.errorBody()?.string()?.let {
                        try {
                            JSONObject(it).getString("message")
                        }catch (e: Exception){
                            "Error closing position"
                        }
                    } ?: "Error closing position"
                    Result.failure(AppError.HttpError(res.code(), errorMessage))
                }else{
                    Result.success(Unit)
                }
            }catch (e: Exception){
                Result.failure(mapError(e))
            }
        }
    }
}