package com.paperledger.app.domain.usecase.funding

import com.paperledger.app.domain.models.funding.Transfer
import com.paperledger.app.domain.repository.ACHRelationshipRepository
import javax.inject.Inject

class GetTransfersUseCase @Inject constructor(private val repository: ACHRelationshipRepository) {
    suspend operator fun invoke(accountId: String): Result<List<Transfer>> {
        return repository.getTransfers(accountId)
    }
}
