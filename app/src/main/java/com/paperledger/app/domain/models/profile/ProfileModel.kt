package com.paperledger.app.domain.models.profile

data class ProfileModel(
    val id: String,
    val accountNumber: String,
    val status: String,
    val currency: String,
    val accountType: String,
    val createdAt: String,
    val lastEquity: String,
    val givenName: String,
    val familyName: String,
    val dateOfBirth: String,
    val countryOfBirth: String,
    val countryOfCitizenship: String,
    val taxIdType: String,
    val fundingSource: List<String>,
    val emailAddress: String,
    val phoneNumber: String,
    val streetAddress: List<String>,
    val city: String,
    val state: String,
    val postalCode: String,
    val trustedContactGivenName: String,
    val trustedContactFamilyName: String,
    val trustedContactEmail: String
)