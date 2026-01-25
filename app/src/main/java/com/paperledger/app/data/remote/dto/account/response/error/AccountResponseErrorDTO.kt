package com.paperledger.app.data.remote.dto.account.response.error

import com.google.gson.annotations.SerializedName

data class AccountResponseErrorDTO(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String
)
