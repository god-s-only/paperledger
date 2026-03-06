package com.paperledger.app.domain.models.ach

data class ACHRelationship(
    val id: String,
    val accountId: String,
    val accountOwnerName: String,
    val bankAccountNumber: String,
    val bankAccountType: String,
    val bankRoutingNumber: String,
    val createdAt: String,
    val nickname: String,
    val status: String,
    val updatedAt: String
)
