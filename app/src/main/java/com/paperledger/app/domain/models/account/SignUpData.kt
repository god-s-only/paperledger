package com.paperledger.app.domain.models.account

data class SignUpData(
    val contact: ContactData = ContactData(),
    val identity: IdentityData = IdentityData(),
    val disclosures: DisclosuresData = DisclosuresData(),
    val documents: DocumentsData = DocumentsData(),
    val trustedContact: TrustedContactData = TrustedContactData(),
    val accountPreferences: AccountPreferencesData = AccountPreferencesData()
)

data class ContactData(
    val emailAddress: String = "",
    val phoneNumber: String = "",
    val streetAddress: List<String> = emptyList(),
    val unit: String = "",
    val city: String = "",
    val state: String = "",
    val postalCode: String = ""
)

data class IdentityData(
    val givenName: String = "",
    val familyName: String = "",
    val dateOfBirth: String = "",
    val countryOfCitizenship: String = "",
    val countryOfBirth: String = "",
    val partyType: String = "",
    val taxId: String = "",
    val taxIdType: String = "",
    val countryOfTaxResidence: String = "",
    val fundingSource: List<String> = listOf("employment_income"),
    val selectedFundingSources: List<String>? = null,
    val selectedAssets: List<String>? = null
)

data class DisclosuresData(
    val isControlPerson: Boolean = false,
    val isAffiliatedExchangeOrFinra: Boolean = false,
    val isAffiliatedExchangeOrIiroc: Boolean = false,
    val isPoliticallyExposed: Boolean = false,
    val immediateFamilyExposed: Boolean = false
)

data class DocumentsData(
    val documentType: String = "",
    val documentSubType: String = "",
    val content: String = ""
)

data class TrustedContactData(
    val givenName: String = "",
    val familyName: String = "",
    val emailAddress: String = ""
)

data class AccountPreferencesData(
    val fundingSource: List<String> = listOf("employment_income"),
    val enabledAssets: List<String> = listOf("us_equity")
)