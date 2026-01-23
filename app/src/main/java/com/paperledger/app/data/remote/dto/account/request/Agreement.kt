package com.paperledger.app.data.remote.dto.account.request


import com.google.gson.annotations.SerializedName

data class Agreement(
    @SerializedName("agreement")
    val agreement: String,
    @SerializedName("ip_address")
    val ipAddress: String,
    @SerializedName("signed_at")
    val signedAt: String
)