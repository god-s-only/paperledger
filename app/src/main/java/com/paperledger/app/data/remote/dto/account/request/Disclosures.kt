package com.paperledger.app.data.remote.dto.account.request


import com.google.gson.annotations.SerializedName

data class Disclosures(
    @SerializedName("immediate_family_exposed")
    val immediateFamilyExposed: Boolean,
    @SerializedName("is_affiliated_exchange_or_finra")
    val isAffiliatedExchangeOrFinra: Boolean,
    @SerializedName("is_control_person")
    val isControlPerson: Boolean,
    @SerializedName("is_politically_exposed")
    val isPoliticallyExposed: Boolean
)