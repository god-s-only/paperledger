package com.paperledger.app.domain.usecase.ach

import com.paperledger.app.domain.models.ach.ACHRelationship
import com.paperledger.app.domain.repository.ACHRelationshipRepository
import javax.inject.Inject

class GetACHRelationshipsUseCase @Inject constructor(private val repository: ACHRelationshipRepository) {
    suspend operator fun invoke(accountId: String): Result<List<ACHRelationship>> {
        return repository.getACHRelationships(accountId)
    }
}
