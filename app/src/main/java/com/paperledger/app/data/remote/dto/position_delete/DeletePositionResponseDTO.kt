package com.paperledger.app.data.remote.dto.position_delete


import com.google.gson.annotations.SerializedName

data class  DeletePositionResponseDTO(
    @SerializedName("asset_class")
    val assetClass: String,
    @SerializedName("asset_id")
    val assetId: String,
    @SerializedName("canceled_at")
    val canceledAt: String,
    @SerializedName("client_order_id")
    val clientOrderId: String,
    @SerializedName("commission")
    val commission: String,
    @SerializedName("commission_bps")
    val commissionBps: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("expired_at")
    val expiredAt: String,
    @SerializedName("extended_hours")
    val extendedHours: Boolean,
    @SerializedName("failed_at")
    val failedAt: String,
    @SerializedName("filled_at")
    val filledAt: String,
    @SerializedName("filled_avg_price")
    val filledAvgPrice: String,
    @SerializedName("filled_qty")
    val filledQty: String,
    @SerializedName("hwm")
    val hwm: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("legs")
    val legs: List<Leg>,
    @SerializedName("limit_price")
    val limitPrice: String,
    @SerializedName("notional")
    val notional: String,
    @SerializedName("order_class")
    val orderClass: String,
    @SerializedName("order_type")
    val orderType: String,
    @SerializedName("qty")
    val qty: String,
    @SerializedName("replaced_at")
    val replacedAt: String,
    @SerializedName("replaced_by")
    val replacedBy: String,
    @SerializedName("replaces")
    val replaces: String,
    @SerializedName("side")
    val side: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("stop_price")
    val stopPrice: String,
    @SerializedName("submitted_at")
    val submittedAt: String,
    @SerializedName("swap_fee_bps")
    val swapFeeBps: String,
    @SerializedName("swap_rate")
    val swapRate: String,
    @SerializedName("symbol")
    val symbol: String,
    @SerializedName("time_in_force")
    val timeInForce: String,
    @SerializedName("trail_percent")
    val trailPercent: String,
    @SerializedName("trail_price")
    val trailPrice: String,
    @SerializedName("type")
    val type: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("usd")
    val usd: Usd
)