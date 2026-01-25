package com.paperledger.app.data.remote.dto.account.response.success


import com.google.gson.annotations.SerializedName

data class Contact(
    @SerializedName("city")
    val city: String,
    @SerializedName("email_address")
    val emailAddress: String,
    @SerializedName("local_street_address")
    val localStreetAddress: Any,
    @SerializedName("phone_number")
    val phoneNumber: String,
    @SerializedName("postal_code")
    val postalCode: String,
    @SerializedName("state")
    val state: String,
    @SerializedName("street_address")
    val streetAddress: List<String>
)