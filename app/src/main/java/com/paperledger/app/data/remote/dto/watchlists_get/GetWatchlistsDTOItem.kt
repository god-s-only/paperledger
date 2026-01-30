package com.paperledger.app.data.remote.dto.watchlists_get


import com.google.gson.annotations.SerializedName

data class GetWatchlistsDTOItem(
    @SerializedName("account_id")
    val accountId: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("updated_at")
    val updatedAt: String
)