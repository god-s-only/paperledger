package com.paperledger.app.data.remote.dto.account.request


import com.google.gson.annotations.SerializedName

data class AccountRequestDTO(
    @SerializedName("account_sub_type")
    val accountSubType: Any? = null,
    @SerializedName("account_type")
    val accountType: String = "",
    @SerializedName("additional_information")
    val additionalInformation: String = "",
    @SerializedName("agreements")
    val agreements: List<Agreement>,
    @SerializedName("authorized_individuals")
    val authorizedIndividuals: Any? = null,
    @SerializedName("auto_approve")
    val autoApprove: Any? = null,
    @SerializedName("beneficiaries")
    val beneficiaries: Any? = null,
    @SerializedName("contact")
    val contact: Contact,
    @SerializedName("currency")
    val currency: Any? = null,
    @SerializedName("disclosures")
    val disclosures: Disclosures,
    @SerializedName("documents")
    val documents: List<Document>,
    @SerializedName("enabled_assets")
    val enabledAssets: Any? = null,
    @SerializedName("entity_id")
    val entityId: Any? = null,
    @SerializedName("identity")
    val identity: Identity,
    @SerializedName("minor_identity")
    val minorIdentity: Any? = null,
    @SerializedName("primary_account_holder_id")
    val primaryAccountHolderId: Any? = null,
    @SerializedName("sub_correspondent")
    val subCorrespondent: Any? = null,
    @SerializedName("trading_configurations")
    val tradingConfigurations: Any? = null,
    @SerializedName("trading_type")
    val tradingType: Any? = null,
    @SerializedName("trusted_contact")
    val trustedContact: TrustedContact,
    @SerializedName("ultimate_beneficial_owners")
    val ultimateBeneficialOwners: Any? = null
)