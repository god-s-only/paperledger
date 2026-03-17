package com.paperledger.app.domain.usecase.funding

import com.paperledger.app.domain.repository.FundingRepository
import javax.inject.Inject

class GetFundingTokenUseCase @Inject constructor(private val repository: FundingRepository) {
    suspend operator fun invoke(): String? {
        return repository.getFundingToken()
    }
}