package com.paperledger.app.domain.repository

import com.paperledger.app.data.remote.dto.pending_order_post.OrderRequestDTO
import com.paperledger.app.data.remote.dto.position_order_post.PositionRequestDTO
import com.paperledger.app.domain.models.trade.AccountInfo
import com.paperledger.app.domain.models.trade.Order
import com.paperledger.app.domain.models.trade.Position
import kotlinx.coroutines.flow.Flow

interface TradesRepository {
    suspend fun getPendingOrders(accountId: String): Result<List<Order>>
    fun getOpenPositions(accountId: String): Flow<Result<List<Position>>>
    suspend fun createPendingOrder(orderRequest: OrderRequestDTO): Result<Unit>
    suspend fun createPositionOrder(positionRequest: PositionRequestDTO): Result<Unit>
    suspend fun closePosition(accountId: String, symbolOrAssetIf: String): Result<Unit>
}