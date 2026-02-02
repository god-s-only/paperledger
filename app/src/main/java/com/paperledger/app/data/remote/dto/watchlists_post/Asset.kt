package com.paperledger.app.data.remote.dto.watchlists_post


import com.google.gson.annotations.SerializedName

data class Asset(
    @SerializedName("attributes")
    val attributes: Any,
    @SerializedName("class")
    val classX: String,
    @SerializedName("easy_to_borrow")
    val easyToBorrow: Boolean,
    @SerializedName("exchange")
    val exchange: String,
    @SerializedName("fractionable")
    val fractionable: Boolean,
    @SerializedName("id")
    val id: String,
    @SerializedName("maintenance_margin_requirement")
    val maintenanceMarginRequirement: Int,
    @SerializedName("marginable")
    val marginable: Boolean,
    @SerializedName("name")
    val name: String,
    @SerializedName("shortable")
    val shortable: Boolean,
    @SerializedName("status")
    val status: String,
    @SerializedName("symbol")
    val symbol: String,
    @SerializedName("tradable")
    val tradable: Boolean
)