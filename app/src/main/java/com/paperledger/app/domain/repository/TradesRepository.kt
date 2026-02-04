package com.paperledger.app.domain.repository

import com.paperledger.app.domain.models.trade.AccountInfo
import com.paperledger.app.domain.models.trade.Order
import com.paperledger.app.domain.models.trade.Position
import kotlinx.coroutines.flow.Flow

interface TradesRepository {
    suspend fun getPendingOrders(accountId: String): Result<List<Order>>
    fun getOpenPositions(accountId: String): Flow<Result<List<Position>>>
    fun getAccountInfo(accountId: String): Flow<Result<AccountInfo>>
}