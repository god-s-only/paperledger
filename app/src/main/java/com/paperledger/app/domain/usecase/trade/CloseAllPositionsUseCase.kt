package com.paperledger.app.domain.usecase.trade

import com.paperledger.app.domain.repository.TradesRepository
import javax.inject.Inject

class CloseAllPositionsUseCase @Inject constructor(private val repository: TradesRepository) {
    suspend operator fun invoke(accountId: String) = repository.closeAllPositions(accountId)
}
