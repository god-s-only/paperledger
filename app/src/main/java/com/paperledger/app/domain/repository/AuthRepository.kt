package com.paperledger.app.domain.repository

interface AuthRepository {
    suspend fun createAccount()
}