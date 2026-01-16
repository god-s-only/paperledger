package com.paperledger.app.data.remote.dto.account.response


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
    @SerializedName("date_of_departure_from_usa")
    val dateOfDepartureFromUsa: Any,
    @SerializedName("family_name")
    val familyName: String,
    @SerializedName("funding_source")
    val fundingSource: List<String>,
    @SerializedName("given_name")
    val givenName: String,
    @SerializedName("investment_experience_with_options")
    val investmentExperienceWithOptions: Any,
    @SerializedName("investment_experience_with_stocks")
    val investmentExperienceWithStocks: Any,
    @SerializedName("investment_time_horizon")
    val investmentTimeHorizon: Any,
    @SerializedName("permanent_resident")
    val permanentResident: Any,
    @SerializedName("tax_id_type")
    val taxIdType: String,
    @SerializedName("visa_expiration_date")
    val visaExpirationDate: Any,
    @SerializedName("visa_type")
    val visaType: Any
)