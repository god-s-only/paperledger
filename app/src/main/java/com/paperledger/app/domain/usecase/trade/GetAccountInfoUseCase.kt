package com.paperledger.app.domain.usecase.trade

import com.paperledger.app.domain.models.trade.AccountInfo
import com.paperledger.app.domain.repository.TradesRepository
import javax.inject.Inject

class GetAccountInfoUseCase @Inject constructor(private val repository: TradesRepository) {
    operator fun invoke(accountId: String) = repository.getAccountInfo(accountId)
}