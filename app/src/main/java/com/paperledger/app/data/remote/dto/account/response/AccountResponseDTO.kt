package com.paperledger.app.data.remote.dto.account.response


import com.google.gson.annotations.SerializedName

data class AccountResponseDTO(
    @SerializedName("account_number")
    val accountNumber: String,
    @SerializedName("account_type")
    val accountType: String,
    @SerializedName("agreements")
    val agreements: List<Agreement>,
    @SerializedName("contact")
    val contact: Contact,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("crypto_status")
    val cryptoStatus: String,
    @SerializedName("currency")
    val currency: String,
    @SerializedName("disclosures")
    val disclosures: Disclosures,
    @SerializedName("enabled_assets")
    val enabledAssets: List<String>,
    @SerializedName("id")
    val id: String,
    @SerializedName("identity")
    val identity: Identity,
    @SerializedName("last_equity")
    val lastEquity: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("trading_configurations")
    val tradingConfigurations: Any,
    @SerializedName("trusted_contact")
    val trustedContact: TrustedContact
)