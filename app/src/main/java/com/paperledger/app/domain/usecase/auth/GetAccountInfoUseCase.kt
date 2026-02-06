package com.paperledger.app.domain.usecase.auth

import com.paperledger.app.domain.models.trade.AccountInfo
import com.paperledger.app.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAccountInfoUseCase @Inject constructor(private val repository: AuthRepository) {
    suspend operator fun invoke(accountId: String): Result<AccountInfo>{
        return repository.getAccountInfo(accountId)
    }
}