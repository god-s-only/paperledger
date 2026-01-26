package com.paperledger.app.domain.repository

import com.paperledger.app.data.remote.dto.ach.request.ACHRelationshipsRequestDTO

interface ACHRelationshipRepository {
    suspend fun createACHRelationship(request: ACHRelationshipsRequestDTO, accountId: String): Result<Unit>
}