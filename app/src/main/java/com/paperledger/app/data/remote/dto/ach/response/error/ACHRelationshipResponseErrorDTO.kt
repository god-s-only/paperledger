package com.paperledger.app.data.remote.dto.ach.response.error

import com.google.gson.annotations.SerializedName

data class ACHRelationshipResponseErrorDTO(
    @SerializedName("code")
    val code: Int,
    @SerializedName("message")
    val message: String
)
