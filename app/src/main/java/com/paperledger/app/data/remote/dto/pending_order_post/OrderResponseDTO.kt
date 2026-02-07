package com.paperledger.app.data.remote.dto.pending_order_post


import com.google.gson.annotations.SerializedName

data class OrderResponseDTO(
    @SerializedName("asset_class")
    val assetClass: String,
    @SerializedName("asset_id")
    val assetId: String,
    @SerializedName("canceled_at")
    val canceledAt: Any,
    @SerializedName("client_order_id")
    val clientOrderId: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("expired_at")
    val expiredAt: Any,
    @SerializedName("extended_hours")
    val extendedHours: Boolean,
    @SerializedName("failed_at")
    val failedAt: Any,
    @SerializedName("filled_at")
    val filledAt: Any,
    @SerializedName("filled_avg_price")
    val filledAvgPrice: Any,
    @SerializedName("filled_qty")
    val filledQty: String,
    @SerializedName("hwm")
    val hwm: Any,
    @SerializedName("id")
    val id: String,
    @SerializedName("legs")
    val legs: Any,
    @SerializedName("limit_price")
    val limitPrice: String,
    @SerializedName("notional")
    val notional: Any,
    @SerializedName("order_class")
    val orderClass: String,
    @SerializedName("order_type")
    val orderType: String,
    @SerializedName("qty")
    val qty: String,
    @SerializedName("replaced_at")
    val replacedAt: Any,
    @SerializedName("replaced_by")
    val replacedBy: Any,
    @SerializedName("replaces")
    val replaces: Any,
    @SerializedName("side")
    val side: String,
    @SerializedName("source")
    val source: Any,
    @SerializedName("status")
    val status: String,
    @SerializedName("stop_price")
    val stopPrice: Any,
    @SerializedName("submitted_at")
    val submittedAt: String,
    @SerializedName("subtag")
    val subtag: Any,
    @SerializedName("symbol")
    val symbol: String,
    @SerializedName("time_in_force")
    val timeInForce: String,
    @SerializedName("trail_percent")
    val trailPercent: Any,
    @SerializedName("trail_price")
    val trailPrice: Any,
    @SerializedName("type")
    val type: String,
    @SerializedName("updated_at")
    val updatedAt: String
)