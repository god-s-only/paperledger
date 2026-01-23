package com.paperledger.app.domain.repository

interface AuthRepository {
    suspend fun createAccount(accountRequest: AccountRequestDTO): Result<String>
    suspend fun updateAccount(accountId: String, accountRequest: AccountRequestDTO): Result<Unit>
    suspend fun storeUserId(userId: String): Result<Unit>
    suspend fun getUserId(): String?
}