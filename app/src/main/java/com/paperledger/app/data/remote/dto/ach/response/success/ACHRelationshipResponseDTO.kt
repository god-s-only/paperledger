package com.paperledger.app.data.remote.dto.ach.response.success

import com.google.gson.annotations.SerializedName

data class ACHRelationshipResponseDTO(
    @SerializedName("account_id")
    val accountId: String,
    @SerializedName("account_owner_name")
    val accountOwnerName: String,
    @SerializedName("bank_account_number")
    val bankAccountNumber: String,
    @SerializedName("bank_account_type")
    val bankAccountType: String,
    @SerializedName("bank_routing_number")
    val bankRoutingNumber: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("nickname")
    val nickname: String,
    @SerializedName("processor_token")
    val processorToken: Any,
    @SerializedName("status")
    val status: String,
    @SerializedName("updated_at")
    val updatedAt: String
)