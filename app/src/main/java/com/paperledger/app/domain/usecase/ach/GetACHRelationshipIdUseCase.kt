package com.paperledger.app.domain.usecase.ach

import com.paperledger.app.domain.repository.ACHRelationshipRepository
import javax.inject.Inject

class GetACHRelationshipIdUseCase @Inject constructor(private val repository: ACHRelationshipRepository) {
    suspend operator fun invoke(accountId: String) = repository.getACHRelationshipId(accountId)
}