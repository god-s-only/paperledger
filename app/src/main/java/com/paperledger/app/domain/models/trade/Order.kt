package com.paperledger.app.domain.models.trade

data class Order(
    val id: String,
    val clientOrderId: String,
    val symbol: String,
    val side: String,
    val type: String,
    val orderClass: String,
    val timeInForce: String,
    val status: String,
    val createdAt: String,
    val updatedAt: String,
    val submittedAt: String,
    val filledAt: String?,
    val expiredAt: String?,
    val canceledAt: String?,
    val quantity: Double,
    val filledQty: Double,
    val limitPrice: Double?,
    val commission: Double,
    val assetClass: String
)