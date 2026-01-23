package com.paperledger.app.presentation.ui.features.auth.signup

data class SignUpState(
    // UI States
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,

    // Account Flow Tracking
    val accountId: String? = null,
    val currentPage: Int = 1,
    val totalPages: Int = 5,
    val canNavigateNext: Boolean = false,

    // Identity Information
    val firstName: String = "",
    val lastName: String = "",
    val dateOfBirth: String = "",
    val taxId: String = "",
    val countryCode: String = "",
    val fundingSource: List<String> = listOf("employment_income"),

    // Contact Information
    val email: String = "",
    val phoneNumber: String = "",
    val streetAddress: List<String> = emptyList(), // changed to List<String>
    val unit: String = "",
    val city: String = "",
    val state: String = "",
    val postalCode: String = "",

    // Agreement Terms
    val accountAgreed: Boolean = false,
    val customerAgreed: Boolean = false,
    val marginAgreed: Boolean = false,
    val optionsAgreed: Boolean = false,

    // Enabled Assets
    val enabledAssets: List<String> = listOf("us_equity"),

    // Trusted Contact Information
    val hasTrustedContact: Boolean = true,
    val trustedContactName: String = "",
    val trustedContactEmail: String = "",

    // Disclosure Information
    val isControlPerson: Boolean = false,
    val isAffiliatedExchange: Boolean = false,
    val isPoliticallyExposed: Boolean = false,
    val immediateFamilyExposed: Boolean = false,

    // Document Uploads
    val uploadedDocuments: List<String> = emptyList()
)
