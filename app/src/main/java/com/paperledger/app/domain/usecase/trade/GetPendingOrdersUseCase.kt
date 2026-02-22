package com.paperledger.app.domain.usecase.trade

import com.paperledger.app.domain.models.trade.Order
import com.paperledger.app.domain.repository.TradesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPendingOrdersUseCase @Inject constructor(private val repository: TradesRepository) {
    operator fun invoke(accountId: String): Flow<Result<List<Order>>> {
        return repository.getPendingOrders(accountId)
    }
}