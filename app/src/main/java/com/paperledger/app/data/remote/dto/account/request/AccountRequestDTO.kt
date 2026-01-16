package com.paperledger.app.data.remote.dto.account.request


import com.google.gson.annotations.SerializedName

data class AccountRequestDTO(
    @SerializedName("agreements")
    val agreements: List<Agreement>,
    @SerializedName("contact")
    val contact: Contact,
    @SerializedName("disclosures")
    val disclosures: Disclosures,
    @SerializedName("documents")
    val documents: List<Document>,
    @SerializedName("enabled_assets")
    val enabledAssets: List<String>,
    @SerializedName("identity")
    val identity: Identity,
    @SerializedName("trusted_contact")
    val trustedContact: TrustedContact
)