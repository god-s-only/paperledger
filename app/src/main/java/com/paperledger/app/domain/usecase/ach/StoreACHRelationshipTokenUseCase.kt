package com.paperledger.app.domain.usecase.ach

import com.paperledger.app.domain.repository.ACHRelationshipRepository
import javax.inject.Inject

class StoreACHRelationshipTokenUseCase @Inject constructor(private val repository: ACHRelationshipRepository) {
    suspend operator fun invoke(token: String) = repository.storeACHRelationshipToken(token)
}