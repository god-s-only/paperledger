package com.paperledger.app.domain.repository

import com.paperledger.app.data.remote.dto.ach.request.ACHRelationshipsRequestDTO
import com.paperledger.app.domain.models.ach.ACHRelationship
import com.paperledger.app.domain.models.funding.Transfer

interface ACHRelationshipRepository {
    suspend fun createACHRelationship(request: ACHRelationshipsRequestDTO, accountId: String): Result<Unit>
    suspend fun storeACHRelationshipToken(token: String): Result<Unit>
    suspend fun getACHRelationshipId(accountId: String): Result<String>
    suspend fun getACHRelationships(accountId: String): Result<List<ACHRelationship>>
    suspend fun getTransfers(accountId: String): Result<List<Transfer>>
}
