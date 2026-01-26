package com.paperledger.app.domain.usecase.ach

import com.paperledger.app.data.remote.dto.ach.request.ACHRelationshipsRequestDTO
import com.paperledger.app.data.repository.ACHRelationshipRepositoryImpl
import javax.inject.Inject

class CreateACHRelationshipUseCase @Inject constructor(private val repository: ACHRelationshipRepositoryImpl) {
    suspend operator fun invoke(accountId: String, request: ACHRelationshipsRequestDTO): Result<Unit> {
        return repository.createACHRelationship(request, accountId)
    }
}