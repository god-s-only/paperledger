package com.paperledger.app.data.remote.dto.pending_order_post


import com.google.gson.annotations.SerializedName
import com.paperledger.app.data.remote.dto.position_order_post.StopLossDTO
import com.paperledger.app.data.remote.dto.position_order_post.TakeProfitDTO


data class OrderRequestDTO(
    @SerializedName("limit_price")
    val limitPrice: String,
    @SerializedName("qty")
    val qty: String,
    @SerializedName("side")
    val side: String,
    @SerializedName("symbol")
    val symbol: String,
    @SerializedName("time_in_force")
    val timeInForce: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("take_profit")
    val takeProfit: TakeProfitDTO?,
    @SerializedName("stop_loss")
    val stopLoss: StopLossDTO?
)