package com.paperledger.app.data.remote.dto.position_delete


import com.google.gson.annotations.SerializedName

data class Leg(
    @SerializedName("value")
    val value: String
)