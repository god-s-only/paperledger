package com.paperledger.app.data.remote.dto.position_order_post


import com.google.gson.annotations.SerializedName

data class PositionRequestDTO(
    @SerializedName("qty")
    val qty: String,
    @SerializedName("side")
    val side: String,
    @SerializedName("symbol")
    val symbol: String,
    @SerializedName("time_in_force")
    val timeInForce: String,
    @SerializedName("type")
    val type: String
)