package com.paperledger.app.domain.repository

import com.paperledger.app.data.remote.dto.account.request.AccountRequestDTO
import com.paperledger.app.domain.models.trade.AccountInfo
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun createAccount(accountRequest: AccountRequestDTO): Result<String>
    suspend fun updateAccount(accountId: String, accountRequest: AccountRequestDTO): Result<Unit>
    suspend fun storeUserId(userId: String): Result<Unit>
    suspend fun getUserId(): String?
    suspend fun getAccountInfo(accountId: String): Flow<Result<AccountInfo>>
}