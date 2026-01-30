package com.paperledger.app.data.remote.dto.error

import com.google.gson.annotations.SerializedName

data class ErrorResponseDTO(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String
)
