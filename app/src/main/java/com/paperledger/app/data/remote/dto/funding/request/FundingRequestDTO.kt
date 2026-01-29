package com.paperledger.app.data.remote.dto.funding.request


import com.google.gson.annotations.SerializedName

data class FundingRequestDTO(
    @SerializedName("amount")
    val amount: String,
    @SerializedName("direction")
    val direction: String,
    @SerializedName("relationship_id")
    val relationshipId: String,
    @SerializedName("transfer_type")
    val transferType: String
)