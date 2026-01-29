package com.paperledger.app.domain.usecase.funding

import com.paperledger.app.data.remote.dto.funding.request.FundingRequestDTO
import com.paperledger.app.domain.repository.FundingRepository
import javax.inject.Inject

class RequestTransferUseCase @Inject constructor(private val repository: FundingRepository) {
    suspend operator fun invoke(request: FundingRequestDTO, accountId: String): Result<Unit> {
        return repository.requestFunding(request, accountId)
    }
}