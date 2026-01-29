package com.paperledger.app.data.repository

import com.paperledger.app.data.remote.api.AlpacaApiService
import com.paperledger.app.data.remote.dto.funding.request.FundingRequestDTO
import com.paperledger.app.domain.repository.FundingRepository
import javax.inject.Inject

class FundingRepositoryImpl @Inject constructor(private val alpacaApiService: AlpacaApiService): FundingRepository {
    override suspend fun requestFunding(request: FundingRequestDTO): Result<Unit> {
        return try {

        }catch (e: Exception){

        }
    }
}