package com.paperledger.app.data.repository

import android.util.Log
import com.paperledger.app.core.AppError
import com.paperledger.app.core.POLL_INTERVAL_MS
import com.paperledger.app.core.mapError
import com.paperledger.app.data.local.PaperledgerDAO
import com.paperledger.app.data.mappers.account.toDomain
import com.paperledger.app.data.mappers.account.toEntity
import com.paperledger.app.data.mappers.trade.toDomain
import com.paperledger.app.data.mappers.trade.toEntity
import com.paperledger.app.data.remote.api.AlpacaApiService
import com.paperledger.app.data.remote.dto.pending_order_post.OrderRequestDTO
import com.paperledger.app.data.remote.dto.position_order_post.PositionRequestDTO
import com.paperledger.app.domain.models.trade.AccountInfo
import com.paperledger.app.domain.models.trade.Order
import com.paperledger.app.domain.models.trade.Position
import com.paperledger.app.domain.repository.TradesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.isActive
import kotlinx.coroutines.withContext
import okio.IOException
import org.json.JSONObject
import javax.inject.Inject
import kotlin.run
import kotlin.runCatching

class TradesRepositoryImpl @Inject constructor(
    private val alpacaApiService: AlpacaApiService,
    private val dao: PaperledgerDAO
) : TradesRepository {


    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getPendingOrders(accountId: String): Flow<Result<List<Order>>> {
        val local = dao.observeAllOrders().mapLatest { entities ->
            Result.success(entities.map { it.toDomain() })
        }

        val remote = flow {
            while (currentCoroutineContext().isActive) {
                try {
                    val response = alpacaApiService.getPendingOrders(accountId)
                    if (!response.isSuccessful) {
                        val errorMessage = response.errorBody()?.string()?.let {
                            runCatching { JSONObject(it).getString("message") }.getOrDefault("Error fetching orders")
                        } ?: "Error fetching orders"
                        emit(Result.failure(AppError.HttpError(response.code(), errorMessage)))
                    } else {
                        val body = response.body() ?: run {
                            emit(Result.failure(AppError.EmptyBody))
                            return@flow
                        }
                        dao.replaceAllOrders(body.map { it.toEntity() })
                        emit(Result.success(body.map { it.toDomain() }))
                        Log.d("PendingOrders", "Refreshed")
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

        return merge(local, remote)
            .flowOn(Dispatchers.IO)
            .distinctUntilChanged()
    }

    // ── Open Positions ────────────────────────────────────────────────────────

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getOpenPositions(accountId: String): Flow<Result<List<Position>>> {
        val local = dao.observeAllPositions().mapLatest { entities ->
            Result.success(entities.map { it.toDomain() })
        }
        val remote = flow {
            while (currentCoroutineContext().isActive) {
                try {
                    val response = alpacaApiService.getOpenPositions(accountId)
                    if (!response.isSuccessful) {
                        val errorMessage = response.errorBody()?.string()?.let {
                            runCatching { JSONObject(it).getString("message") }.getOrDefault("Error fetching positions")
                        } ?: "Error fetching positions"
                        emit(Result.failure(AppError.HttpError(response.code(), errorMessage)))
                    } else {
                        val body = response.body() ?: run {
                            emit(Result.failure(AppError.EmptyBody))
                            return@flow
                        }
                        dao.replaceAllPositions(body.map { it.toEntity() })
                        emit(Result.success(body.map { it.toDomain() }))
                        Log.d("Positions", "Refreshed")
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

        return merge(local, remote)
            .flowOn(Dispatchers.IO)
            .distinctUntilChanged()
    }

    // ── Mutations (unchanged) ─────────────────────────────────────────────────

    override suspend fun createPendingOrder(accountId: String, orderRequest: OrderRequestDTO): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val res = alpacaApiService.createPendingOrder(accountId, orderRequest)
                if (!res.isSuccessful) {
                    val errorMessage = res.errorBody()?.string()?.let {
                        runCatching { JSONObject(it).getString("message") }.getOrDefault("Error creating pending order")
                    } ?: "Error creating pending order"
                    Result.failure(AppError.HttpError(res.code(), errorMessage))
                } else Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(mapError(e))
            }
        }
    }

    override suspend fun createPositionOrder(accountId: String, positionRequest: PositionRequestDTO): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val res = alpacaApiService.createPositionOrder(accountId, positionRequest)
                if (!res.isSuccessful) {
                    val errorMessage = res.errorBody()?.string()?.let {
                        runCatching { JSONObject(it).getString("message") }.getOrDefault("Error creating position order")
                    } ?: "Error creating position order"
                    Result.failure(AppError.HttpError(res.code(), errorMessage))
                } else Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(mapError(e))
            }
        }
    }

    override suspend fun closePosition(accountId: String, symbolOrAssetId: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val res = alpacaApiService.closePosition(accountId, symbolOrAssetId)
                if (!res.isSuccessful) {
                    val errorMessage = res.errorBody()?.string()?.let {
                        runCatching { JSONObject(it).getString("message") }.getOrDefault("Error closing position")
                    } ?: "Error closing position"
                    Result.failure(AppError.HttpError(res.code(), errorMessage))
                } else Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(mapError(e))
            }
        }
    }

    override suspend fun closePendingOrder(orderId: String, accountId: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val res = alpacaApiService.closePendingOrder(orderId, accountId)
                if (!res.isSuccessful) {
                    val errorMessage = res.errorBody()?.string()?.let {
                        runCatching { JSONObject(it).getString("message") }.getOrDefault("Error closing order")
                    } ?: "Error closing order"
                    Result.failure(AppError.HttpError(res.code(), errorMessage))
                } else Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(mapError(e))
            }
        }
    }

    override suspend fun closeAllPositions(accountId: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val res = alpacaApiService.closeAllPositions(accountId)
                if (!res.isSuccessful) {
                    val errorMessage = res.errorBody()?.string()?.let {
                        runCatching { JSONObject(it).getString("message") }.getOrDefault("Error closing all positions")
                    } ?: "Error closing all positions"
                    Result.failure(AppError.HttpError(res.code(), errorMessage))
                } else Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(mapError(e))
            }
        }
    }

    override suspend fun cancelAllPendingOrders(accountId: String): Result<Unit> {
        return withContext(Dispatchers.IO) {
            try {
                val res = alpacaApiService.cancelAllPendingOrders(accountId)
                if (!res.isSuccessful) {
                    val errorMessage = res.errorBody()?.string()?.let {
                        runCatching { JSONObject(it).getString("message") }.getOrDefault("Error canceling all pending orders")
                    } ?: "Error canceling all pending orders"
                    Result.failure(AppError.HttpError(res.code(), errorMessage))
                } else Result.success(Unit)
            } catch (e: Exception) {
                Result.failure(mapError(e))
            }
        }
    }
}