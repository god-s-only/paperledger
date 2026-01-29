package com.paperledger.app.domain.repository

import com.paperledger.app.data.remote.dto.funding.request.FundingRequestDTO

interface FundingRepository {
    suspend fun requestFunding(request: FundingRequestDTO): Result<Unit>
}