package com.paperledger.app.data.remote.dto.account.response


import com.google.gson.annotations.SerializedName

data class TrustedContact(
    @SerializedName("email_address")
    val emailAddress: String,
    @SerializedName("family_name")
    val familyName: String,
    @SerializedName("given_name")
    val givenName: String
)