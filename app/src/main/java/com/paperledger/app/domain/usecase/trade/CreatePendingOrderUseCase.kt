package com.paperledger.app.domain.usecase.trade

import com.paperledger.app.data.remote.dto.pending_order_post.OrderRequestDTO
import com.paperledger.app.domain.repository.TradesRepository
import javax.inject.Inject

class CreatePendingOrderUseCase @Inject constructor(private val repository: TradesRepository) {
    suspend operator fun invoke(accountId: String, requestDTO: OrderRequestDTO)=
        repository.createPendingOrder(accountId, requestDTO)
}