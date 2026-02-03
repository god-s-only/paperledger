package com.paperledger.app.data.remote.dto.open_positions_get


import com.google.gson.annotations.SerializedName

data class GetOpenPositionResponseDTOItem(
    @SerializedName("asset_class")
    val assetClass: String,
    @SerializedName("asset_id")
    val assetId: String,
    @SerializedName("asset_marginable")
    val assetMarginable: Boolean,
    @SerializedName("avg_entry_price")
    val avgEntryPrice: String,
    @SerializedName("avg_entry_swap_rate")
    val avgEntrySwapRate: String,
    @SerializedName("change_today")
    val changeToday: String,
    @SerializedName("cost_basis")
    val costBasis: String,
    @SerializedName("current_price")
    val currentPrice: String,
    @SerializedName("exchange")
    val exchange: String,
    @SerializedName("lastday_price")
    val lastdayPrice: String,
    @SerializedName("market_value")
    val marketValue: String,
    @SerializedName("qty")
    val qty: String,
    @SerializedName("side")
    val side: String,
    @SerializedName("swap_rate")
    val swapRate: String,
    @SerializedName("symbol")
    val symbol: String,
    @SerializedName("unrealized_intraday_pl")
    val unrealizedIntradayPl: String,
    @SerializedName("unrealized_intraday_plpc")
    val unrealizedIntradayPlpc: String,
    @SerializedName("unrealized_pl")
    val unrealizedPl: String,
    @SerializedName("unrealized_plpc")
    val unrealizedPlpc: String,
    @SerializedName("usd")
    val usd: Usd
)