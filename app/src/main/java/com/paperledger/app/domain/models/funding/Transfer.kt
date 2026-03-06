package com.paperledger.app.domain.models.funding

data class Transfer(
    val id: String,
    val accountId: String,
    val amount: String,
    val createdAt: String,
    val currency: String,
    val direction: String,
    val expiresAt: String,
    val fee: String,
    val feePaymentMethod: String,
    val instantAmount: String,
    val relationshipId: String,
    val requestedAmount: String,
    val status: String,
    val type: String,
    val updatedAt: String
)
