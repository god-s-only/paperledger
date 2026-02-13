package com.paperledger.app.data.remote.dto.pending_orders_get


import com.google.gson.annotations.SerializedName

data class GetPendingOrdersResponseDTOItem(
    @SerializedName("asset_class")
    val assetClass: String,
    @SerializedName("asset_id")
    val assetId: String,
    @SerializedName("canceled_at")
    val canceledAt: String? = null,
    @SerializedName("client_order_id")
    val clientOrderId: String,
    @SerializedName("commission")
    val commission: String? = null,
    @SerializedName("commission_bps")
    val commissionBps: String? = null,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("expired_at")
    val expiredAt: String? = null,
    @SerializedName("extended_hours")
    val extendedHours: Boolean,
    @SerializedName("failed_at")
    val failedAt: String? = null,
    @SerializedName("filled_at")
    val filledAt: String? = null,
    @SerializedName("filled_avg_price")
    val filledAvgPrice: String? = null,
    @SerializedName("filled_qty")
    val filledQty: String,
    @SerializedName("hwm")
    val hwm: String? = null,
    @SerializedName("id")
    val id: String,
    @SerializedName("legs")
    val legs: List<Leg>? = null,
    @SerializedName("limit_price")
    val limitPrice: String? = null,
    @SerializedName("notional")
    val notional: String? = null,
    @SerializedName("order_class")
    val orderClass: String,
    @SerializedName("order_type")
    val orderType: String,
    @SerializedName("qty")
    val qty: String,
    @SerializedName("replaced_at")
    val replacedAt: String? = null,
    @SerializedName("replaced_by")
    val replacedBy: String? = null,
    @SerializedName("replaces")
    val replaces: String? = null,
    @SerializedName("side")
    val side: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("stop_price")
    val stopPrice: String? = null,
    @SerializedName("submitted_at")
    val submittedAt: String,
    @SerializedName("swap_fee_bps")
    val swapFeeBps: String? = null,
    @SerializedName("swap_rate")
    val swapRate: String? = null,
    @SerializedName("symbol")
    val symbol: String,
    @SerializedName("time_in_force")
    val timeInForce: String,
    @SerializedName("trail_percent")
    val trailPercent: String? = null,
    @SerializedName("trail_price")
    val trailPrice: String? = null,
    @SerializedName("type")
    val type: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("usd")
    val usd: Usd? = null
)