package com.paperledger.app.domain.usecase.trade

import com.paperledger.app.domain.models.trade.Position
import com.paperledger.app.domain.repository.TradesRepository
import javax.inject.Inject

class git GetOpenPositionsUseCase @Inject constructor(private val repository: TradesRepository) {
    operator fun invoke(accountId: String) = repository.getOpenPositions(accountId)
}