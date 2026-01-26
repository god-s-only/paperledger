package com.paperledger.app.data.remote.dto.ach.request


import com.google.gson.annotations.SerializedName

data class ACHRelationshipsRequestDTO(
    @SerializedName("account_owner_name")
    val accountOwnerName: String,
    @SerializedName("bank_account_number")
    val bankAccountNumber: String,
    @SerializedName("bank_account_type")
    val bankAccountType: String,
    @SerializedName("bank_routing_number")
    val bankRoutingNumber: String,
    @SerializedName("nickname")
    val nickname: String
)