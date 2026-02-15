package com.paperledger.app.domain.usecase.trade

import com.paperledger.app.data.remote.dto.position_order_post.PositionRequestDTO
import com.paperledger.app.domain.repository.TradesRepository
import jakarta.inject.Inject

class CreatePositionOrderUseCase @Inject constructor(private val repository: TradesRepository) {
    suspend operator fun invoke(accountId: String, positionRequest: PositionRequestDTO) = repository.createPositionOrder(accountId, positionRequest)
}