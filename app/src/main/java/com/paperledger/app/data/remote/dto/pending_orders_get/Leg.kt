package com.paperledger.app.data.remote.dto.pending_orders_get


import com.google.gson.annotations.SerializedName

data class Leg(
    @SerializedName("value")
    val value: String
)