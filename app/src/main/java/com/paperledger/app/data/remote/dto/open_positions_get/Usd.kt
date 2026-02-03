package com.paperledger.app.data.remote.dto.open_positions_get


import com.google.gson.annotations.SerializedName

data class Usd(
    @SerializedName("avg_entry_price")
    val avgEntryPrice: String,
    @SerializedName("change_today")
    val changeToday: String,
    @SerializedName("cost_basis")
    val costBasis: String,
    @SerializedName("current_price")
    val currentPrice: String,
    @SerializedName("lastday_price")
    val lastdayPrice: String,
    @SerializedName("market_value")
    val marketValue: String,
    @SerializedName("unrealized_intraday_pl")
    val unrealizedIntradayPl: String,
    @SerializedName("unrealized_intraday_plpc")
    val unrealizedIntradayPlpc: String,
    @SerializedName("unrealized_pl")
    val unrealizedPl: String,
    @SerializedName("unrealized_plpc")
    val unrealizedPlpc: String
)