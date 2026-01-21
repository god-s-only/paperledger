package com.paperledger.app.domain.repository

import com.paperledger.app.data.remote.dto.account.request.AccountRequestDTO

interface AuthRepository {
    suspend fun createAccount(accountRequest: AccountRequestDTO): Result<String>
    suspend fun updateAccount(accountId: String, accountRequest: AccountRequestDTO): Result<Unit>
    suspend fun storeUserId(userId: String): Result<Unit>
}