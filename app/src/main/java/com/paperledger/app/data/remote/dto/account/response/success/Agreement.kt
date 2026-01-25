package com.paperledger.app.data.remote.dto.account.response.success


import com.google.gson.annotations.SerializedName

data class Agreement(
    @SerializedName("agreement")
    val agreement: String,
    @SerializedName("ip_address")
    val ipAddress: String,
    @SerializedName("revision")
    val revision: String,
    @SerializedName("signed_at")
    val signedAt: String
)