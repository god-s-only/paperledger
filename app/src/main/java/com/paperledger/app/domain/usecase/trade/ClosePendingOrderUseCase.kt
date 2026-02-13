package com.paperledger.app.domain.usecase.trade

import com.paperledger.app.domain.repository.TradesRepository
import javax.inject.Inject

class ClosePendingOrderUseCase @Inject constructor(private val repository: TradesRepository) {
    suspend operator fun invoke(orderId: String, accountId: String) = repository.closePendingOrder(orderId, accountId)
}