package com.paperledger.app.presentation.ui.features.auth.signup

sealed class SignUpEvent {

    // Identity Information
    data class OnFirstNameChange(val firstName: String) : SignUpEvent()
    data class OnLastNameChange(val lastName: String) : SignUpEvent()
    data class OnDateOfBirthChange(val dateOfBirth: String) : SignUpEvent()
    data class OnTaxIdChange(val taxId: String) : SignUpEvent()
    data class OnCountryCodeChange(val countryCode: String) : SignUpEvent()

    // Contact Information
    data class OnEmailChange(val email: String) : SignUpEvent()
    data class OnPhoneNumberChange(val phoneNumber: String) : SignUpEvent()
    data class OnStreetAddressChange(val streetAddress: String) : SignUpEvent()
    data class OnCityChange(val city: String) : SignUpEvent()
    data class OnStateChange(val state: String) : SignUpEvent()
    data class OnPostalCodeChange(val postalCode: String) : SignUpEvent()

    // Account Preferences
    data class OnAccountTypeChange(val accountType: String) : SignUpEvent()
    data class OnFundingSourceChange(val fundingSource: String) : SignUpEvent()

    // Agreement Terms
    data class OnAccountAgreementChange(val agreed: Boolean) : SignUpEvent()
    data class OnCustomerAgreementChange(val agreed: Boolean) : SignUpEvent()
    data class OnMarginAgreementChange(val agreed: Boolean) : SignUpEvent()
    data class OnOptionsAgreementChange(val agreed: Boolean) : SignUpEvent()

    // Trusted Contact Information
    data class OnTrustedContactGivenChange(val hasTrustedContact: Boolean) : SignUpEvent()
    data class OnTrustedContactNameChange(val name: String) : SignUpEvent()
    data class OnTrustedContactPhoneChange(val phone: String) : SignUpEvent()

    // Disclosure Information
    data class OnIsControlPersonChange(val isControlPerson: Boolean) : SignUpEvent()
    data class OnIsAffiliatedExchangeChange(val isAffiliatedExchange: Boolean) : SignUpEvent()
    data class OnIsPoliticallyExposedChange(val isPoliticallyExposed: Boolean) : SignUpEvent()
    data class OnImmediateFamilyExposedChange(val immediateFamilyExposed: Boolean) : SignUpEvent()

    // Document Uploads
    data class OnDocumentUploaded(val documentId: String) : SignUpEvent()
    data class OnDocumentRemoved(val documentId: String) : SignUpEvent()

    // Page Navigation - Incremental Account Flow
    // Page 1: Identity → Create Account
    data object OnNextFromIdentityPage : SignUpEvent()

    // Page 2: Contact → Update Account  
    data object OnNextFromContactPage : SignUpEvent()

    // Page 3: Account Preferences → Update Account
    data object OnNextFromPreferencesPage : SignUpEvent()

    // Page 4: Agreements → Update Account
    data object OnNextFromAgreementsPage : SignUpEvent()

    // Page 5: Trusted Contact → Update Account
    data object OnNextFromTrustedContactPage : SignUpEvent()

    // Page 6: Disclosures → Update Account
    data object OnNextFromDisclosuresPage : SignUpEvent()

    // Page 7: Documents → Update Account (Final)
    data object OnSubmitFromDocumentsPage : SignUpEvent()

    // Navigation Actions
    data object OnNavigateToPreviousPage : SignUpEvent()
    data object OnNavigateToNextPage : SignUpEvent()
    data object OnNavigateBack : SignUpEvent()
    data object OnRetrySubmit : SignUpEvent()

}