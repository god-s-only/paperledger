package com.paperledger.app.domain.usecase.funding

import com.paperledger.app.domain.repository.FundingRepository
import jakarta.inject.Inject

class StoreFundingTokenUseCase @Inject constructor(private val repository: FundingRepository) {
    suspend operator fun invoke(token: String): Result<Unit> {
        return repository.storeFundingToken(token)
    }
}