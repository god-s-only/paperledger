package com.paperledger.app.data.remote.dto.position_order_post

import com.google.gson.annotations.SerializedName

data class StopLossDTO(
    @SerializedName("stop_price")
    val stopPrice: String,
    @SerializedName("limit_price")
    val limitPrice: String
)
