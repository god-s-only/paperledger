package com.paperledger.app.domain.usecase.auth

import com.paperledger.app.data.remote.dto.account.request.AccountRequestDTO
import com.paperledger.app.domain.repository.AuthRepository
import javax.inject.Inject

class UpdateAccountUseCase @Inject constructor(private val authRepository: AuthRepository) {
    suspend operator fun invoke(accountId: String, accountRequest: AccountRequestDTO): Result<Unit>{
        return authRepository.updateAccount(accountId, accountRequest)
    }
}