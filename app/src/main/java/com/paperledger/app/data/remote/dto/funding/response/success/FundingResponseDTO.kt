package com.paperledger.app.data.remote.dto.funding.response.success


import com.google.gson.annotations.SerializedName

data class FundingResponseDTO(
    @SerializedName("account_id")
    val accountId: String,
    @SerializedName("amount")
    val amount: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("currency")
    val currency: String,
    @SerializedName("direction")
    val direction: String,
    @SerializedName("expires_at")
    val expiresAt: String,
    @SerializedName("fee")
    val fee: String,
    @SerializedName("fee_payment_method")
    val feePaymentMethod: String,
    @SerializedName("hold_until")
    val holdUntil: Any,
    @SerializedName("id")
    val id: String,
    @SerializedName("instant_amount")
    val instantAmount: String,
    @SerializedName("reason")
    val reason: Any,
    @SerializedName("relationship_id")
    val relationshipId: String,
    @SerializedName("requested_amount")
    val requestedAmount: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("updated_at")
    val updatedAt: String
)