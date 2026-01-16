package com.paperledger.app.data.remote.dto.assets


import com.google.gson.annotations.SerializedName

data class AssetsResponseDTOItem(
    @SerializedName("attributes")
    val attributes: List<String>,
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
    @SerializedName("margin_requirement_long")
    val marginRequirementLong: String,
    @SerializedName("margin_requirement_short")
    val marginRequirementShort: String,
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