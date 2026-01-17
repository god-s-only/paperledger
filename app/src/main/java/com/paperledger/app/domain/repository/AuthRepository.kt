package com.paperledger.app.domain.repository

import com.paperledger.app.data.remote.dto.account.request.AccountRequestDTO

interface AuthRepository {
    suspend fun createAccount(accountRequest: AccountRequestDTO): Result<String>
}