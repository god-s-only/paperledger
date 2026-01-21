package com.paperledger.app.domain.usecase.auth

import com.paperledger.app.domain.repository.AuthRepository
import javax.inject.Inject

class StoreUserIdUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(userId: String): Result<Unit>{
        return authRepository.storeUserId(userId)
    }
}