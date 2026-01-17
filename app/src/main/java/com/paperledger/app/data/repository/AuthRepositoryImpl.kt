package com.paperledger.app.data.repository

import com.paperledger.app.data.remote.api.AlpacaApiService
import com.paperledger.app.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val alpacaApi: AlpacaApiService): AuthRepository {
    override suspend fun createAccount() {
        TODO("Not yet implemented")
    }
}