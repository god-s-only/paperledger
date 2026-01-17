package com.paperledger.app.presentation.ui.features.auth.signup

data class SignUpState(
    // UI States
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,

    // Identity Information
    val firstName: String = "",
    val lastName: String = "",
    val dateOfBirth: String = "",
    val taxId: String = "",
    val countryCode: String = "",

    // Contact Information
    val email: String = "",
    val phoneNumber: String = "",
    val streetAddress: String = "",
    val city: String = "",
    val state: String = "",
    val postalCode: String = "",

    // Account Preferences
    val accountType: String = "",
    val fundingSource: String = "",

    // Agreement Terms
    val accountAgreed: Boolean = false,
    val customerAgreed: Boolean = false,
    val marginAgreed: Boolean = false,
    val optionsAgreed: Boolean = false,

    val hasTrustedContact: Boolean = false,
    val trustedContactName: String = "",
    val trustedContactPhone: String = "",

    // Disclosure Information
    val isControlPerson: Boolean = false,
    val isAffiliatedExchange: Boolean = false,
    val isPoliticallyExposed: Boolean = false,
    val immediateFamilyExposed: Boolean = false,

    // Document Uploads
    val uploadedDocuments: List<String> = emptyList()
)
