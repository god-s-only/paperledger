package com.paperledger.app.domain.usecase.auth

import com.paperledger.app.domain.repository.AuthRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(accountRequest: AccountRequestDTO): Result<String> {
        return authRepository.createAccount(accountRequest)
    }
}