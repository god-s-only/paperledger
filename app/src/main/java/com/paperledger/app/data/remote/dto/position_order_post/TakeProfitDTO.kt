package com.paperledger.app.data.remote.dto.position_order_post

import com.google.gson.annotations.SerializedName

data class TakeProfitDTO(
    @SerializedName("limit_price")
    val limitPrice: String
)