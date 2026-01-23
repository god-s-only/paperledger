package com.paperledger.app.data.remote.dto.account.request


import com.google.gson.annotations.SerializedName

data class Identity(
    @SerializedName("country_of_birth")
    val countryOfBirth: String,
    @SerializedName("country_of_citizenship")
    val countryOfCitizenship: String,
    @SerializedName("country_of_tax_residence")
    val countryOfTaxResidence: String,
    @SerializedName("date_of_birth")
    val dateOfBirth: String,
    @SerializedName("family_name")
    val familyName: String,
    @SerializedName("funding_source")
    val fundingSource: List<String>,
    @SerializedName("given_name")
    val givenName: String,
    @SerializedName("party_type")
    val partyType: String = "",
    @SerializedName("tax_id")
    val taxId: String,
    @SerializedName("tax_id_type")
    val taxIdType: String
)