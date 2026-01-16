package com.paperledger.app.data.remote.dto.account.request


import com.google.gson.annotations.SerializedName

data class TrustedContact(
    @SerializedName("email_address")
    val emailAddress: String,
    @SerializedName("family_name")
    val familyName: String,
    @SerializedName("given_name")
    val givenName: String
)