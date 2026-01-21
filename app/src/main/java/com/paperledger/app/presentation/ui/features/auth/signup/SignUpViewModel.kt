package com.paperledger.app.presentation.ui.features.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paperledger.app.core.AppError
import com.paperledger.app.core.UIEvent
import com.paperledger.app.data.remote.dto.account.request.*
import com.paperledger.app.domain.usecase.auth.SignUpUseCase
import com.paperledger.app.domain.usecase.auth.StoreUserIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase,
    private val storeUserIdUseCase: StoreUserIdUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(SignUpState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: SignUpEvent) {
        when (event) {
            SignUpEvent.OnNextFromContactPage -> {
                if (isContactPageValid()) {
                    viewModelScope.launch {
                        _state.value = _state.value.copy(isLoading = true, error = null)
                        val contactRequest = buildContactRequestDTO(_state.value)

                        signUpUseCase(contactRequest).fold(
                            onSuccess = { accountId ->
                                _state.value = _state.value.copy(
                                    accountId = accountId,
                                    currentPage = 2,
                                    isLoading = false
                                )
                                storeUserIdUseCase(accountId)
                            },
                            onFailure = { error ->
                                _state.update { it.copy(isLoading = false, error = error.message) }
                            }
                        )
                    }
                } else {
                    _state.value =
                        _state.value.copy(error = "Please complete all required contact fields")
                }
            }

            SignUpEvent.OnNextFromIdentityPage -> {
                if (isIdentityPageValid()) {
                    viewModelScope.launch {
                        _state.value = _state.value.copy(isLoading = true, error = null)
                        val identityRequest = buildIdentityRequestDTO(_state.value)
                        _state.value = _state.value.copy(currentPage = 3, isLoading = false)
                    }
                } else {
                    _state.value =
                        _state.value.copy(error = "Please complete all required identity fields")
                }
            }

            SignUpEvent.OnNextFromPreferencesPage -> {
                if (isPreferencesPageValid()) {
                    viewModelScope.launch {
                        _state.value = _state.value.copy(isLoading = true, error = null)
                        val preferencesRequest = buildPreferencesRequestDTO(_state.value)

                        _state.value = _state.value.copy(currentPage = 4, isLoading = false)
                    }
                } else {
                    _state.value =
                        _state.value.copy(error = "Please select account type and funding source")
                }
            }

            SignUpEvent.OnNextFromAgreementsPage -> {
                if (areAgreementsValid()) {
                    viewModelScope.launch {
                        _state.value = _state.value.copy(isLoading = true, error = null)
                        val agreementsRequest = buildAgreementsRequestDTO(_state.value)

                        // TODO: Call updateAccount when implemented
                        _state.value = _state.value.copy(currentPage = 5, isLoading = false)
                    }
                } else {
                    _state.value =
                        _state.value.copy(error = "Please accept all required agreements")
                }
            }

            SignUpEvent.OnNextFromTrustedContactPage -> {
                if (isTrustedContactValid()) {
                    viewModelScope.launch {
                        _state.value = _state.value.copy(isLoading = true, error = null)
                        val trustedContactRequest = buildTrustedContactRequestDTO(_state.value)
                        _state.value = _state.value.copy(currentPage = 6, isLoading = false)
                    }
                } else {
                    _state.value =
                        _state.value.copy(error = "Please complete trusted contact information")
                }
            }

            SignUpEvent.OnNextFromDisclosuresPage -> {
                viewModelScope.launch {
                    _state.value = _state.value.copy(isLoading = true, error = null)
                    val disclosuresRequest = buildDisclosuresRequestDTO(_state.value)

                    // TODO: Call updateAccount when implemented
                    _state.value = _state.value.copy(currentPage = 7, isLoading = false)
                }
            }

            SignUpEvent.OnSubmitFromDocumentsPage -> {
                viewModelScope.launch {
                    _state.value = _state.value.copy(isLoading = true, error = null)
                    val documentsRequest = buildDocumentsRequestDTO(_state.value)

                    _state.value = _state.value.copy(isLoading = false, isSuccess = true)
                }
            }

            SignUpEvent.OnNavigateToPreviousPage -> {
                if (_state.value.currentPage > 1) {
                    _state.value = _state.value.copy(
                        currentPage = _state.value.currentPage - 1,
                        error = null
                    )
                }
            }

            SignUpEvent.OnNavigateToNextPage -> {
                if (_state.value.currentPage < _state.value.totalPages) {
                    _state.value = _state.value.copy(
                        currentPage = _state.value.currentPage + 1,
                        error = null
                    )
                }
            }

            SignUpEvent.OnNavigateBack -> {
                _state.value = _state.value.copy(error = null)
            }

            SignUpEvent.OnRetrySubmit -> {
                _state.value = _state.value.copy(error = null)
            }

            is SignUpEvent.OnFirstNameChange -> {
                _state.value = _state.value.copy(firstName = event.firstName)
                updateCanNavigateNext()
            }

            is SignUpEvent.OnLastNameChange -> {
                _state.value = _state.value.copy(lastName = event.lastName)
                updateCanNavigateNext()
            }

            is SignUpEvent.OnDateOfBirthChange -> {
                _state.value = _state.value.copy(dateOfBirth = event.dateOfBirth)
                updateCanNavigateNext()
            }

            is SignUpEvent.OnTaxIdChange -> {
                _state.value = _state.value.copy(taxId = event.taxId)
            }

            is SignUpEvent.OnCountryCodeChange -> {
                _state.value = _state.value.copy(countryCode = event.countryCode)
            }

            is SignUpEvent.OnEmailChange -> {
                _state.value = _state.value.copy(email = event.email, error = null)
                updateCanNavigateNext()
            }

            is SignUpEvent.OnPhoneNumberChange -> {
                _state.value = _state.value.copy(phoneNumber = event.phoneNumber)
                updateCanNavigateNext()
            }

            is SignUpEvent.OnStreetAddressChange -> {
                _state.value = _state.value.copy(streetAddress = event.streetAddress)
                updateCanNavigateNext()
            }

            is SignUpEvent.OnCityChange -> {
                _state.value = _state.value.copy(city = event.city)
                updateCanNavigateNext()
            }

            is SignUpEvent.OnStateChange -> {
                _state.value = _state.value.copy(state = event.state)
                updateCanNavigateNext()
            }

            is SignUpEvent.OnPostalCodeChange -> {
                _state.value = _state.value.copy(postalCode = event.postalCode)
                updateCanNavigateNext()
            }

            is SignUpEvent.OnAccountTypeChange -> {
                _state.value = _state.value.copy(accountType = event.accountType)
                updateCanNavigateNext()
            }

            is SignUpEvent.OnFundingSourceChange -> {
                _state.value = _state.value.copy(fundingSource = event.fundingSource)
                updateCanNavigateNext()
            }

            is SignUpEvent.OnAccountAgreementChange -> {
                _state.value = _state.value.copy(accountAgreed = event.agreed, error = null)
                updateCanNavigateNext()
            }

            is SignUpEvent.OnCustomerAgreementChange -> {
                _state.value = _state.value.copy(customerAgreed = event.agreed, error = null)
                updateCanNavigateNext()
            }

            is SignUpEvent.OnMarginAgreementChange -> {
                _state.value = _state.value.copy(marginAgreed = event.agreed, error = null)
                updateCanNavigateNext()
            }

            is SignUpEvent.OnOptionsAgreementChange -> {
                _state.value = _state.value.copy(optionsAgreed = event.agreed, error = null)
                updateCanNavigateNext()
            }

            is SignUpEvent.OnTrustedContactGivenChange -> {
                _state.value = _state.value.copy(hasTrustedContact = event.hasTrustedContact)
                updateCanNavigateNext()
            }

            is SignUpEvent.OnTrustedContactNameChange -> {
                _state.value = _state.value.copy(trustedContactName = event.name)
                updateCanNavigateNext()
            }

            is SignUpEvent.OnTrustedContactPhoneChange -> {
                _state.value = _state.value.copy(trustedContactPhone = event.phone)
                updateCanNavigateNext()
            }

            is SignUpEvent.OnIsControlPersonChange -> {
                _state.value = _state.value.copy(isControlPerson = event.isControlPerson)
            }

            is SignUpEvent.OnIsAffiliatedExchangeChange -> {
                _state.value = _state.value.copy(isAffiliatedExchange = event.isAffiliatedExchange)
            }

            is SignUpEvent.OnIsPoliticallyExposedChange -> {
                _state.value = _state.value.copy(isPoliticallyExposed = event.isPoliticallyExposed)
            }

            is SignUpEvent.OnImmediateFamilyExposedChange -> {
                _state.value =
                    _state.value.copy(immediateFamilyExposed = event.immediateFamilyExposed)
            }

            is SignUpEvent.OnDocumentUploaded -> {
                _state.value = _state.value.copy(
                    uploadedDocuments = _state.value.uploadedDocuments + event.documentId
                )
            }

            is SignUpEvent.OnDocumentRemoved -> {
                _state.value = _state.value.copy(
                    uploadedDocuments = _state.value.uploadedDocuments - event.documentId
                )
            }
        }
    }
    private fun isIdentityPageValid(): Boolean {
        val state = _state.value
        return state.firstName.isNotBlank() &&
                state.lastName.isNotBlank() &&
                state.dateOfBirth.isNotBlank() &&
                state.countryCode.isNotBlank()
    }

    private fun isContactPageValid(): Boolean {
        val state = _state.value
        return state.email.isNotBlank() &&
                state.phoneNumber.isNotBlank() &&
                state.streetAddress.isNotBlank() &&
                state.city.isNotBlank() &&
                state.state.isNotBlank() &&
                state.postalCode.isNotBlank()
    }

    private fun isPreferencesPageValid(): Boolean {
        val state = _state.value
        return state.accountType.isNotBlank() &&
                state.fundingSource.isNotBlank()
    }

    private fun areAgreementsValid(): Boolean {
        val state = _state.value
        return state.accountAgreed && state.customerAgreed
    }

    private fun isTrustedContactValid(): Boolean {
        val state = _state.value
        if (!state.hasTrustedContact) return true

        return state.trustedContactName.isNotBlank() &&
                state.trustedContactPhone.isNotBlank()
    }

    private fun updateCanNavigateNext() {
        val current = _state.value
        val canNavigate = when (current.currentPage) {
            1 -> isContactPageValid()
            2 -> isIdentityPageValid()
            3 -> isPreferencesPageValid()
            4 -> areAgreementsValid()
            5 -> isTrustedContactValid()
            6 -> true
            7 -> current.uploadedDocuments.isNotEmpty()
            else -> false
        }
        _state.value = current.copy(canNavigateNext = canNavigate)
    }

    private fun sendUIEvent(event: UIEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
    private fun buildIdentityRequestDTO(state: SignUpState): AccountRequestDTO {
        val identity = Identity(
            givenName = state.firstName,
            familyName = state.lastName,
            dateOfBirth = state.dateOfBirth,
            taxId = state.taxId.ifEmpty { "" },
            taxIdType = "USA_SSN",
            countryOfBirth = state.countryCode,
            countryOfCitizenship = state.countryCode,
            countryOfTaxResidence = state.countryCode,
            fundingSource = listOf(state.fundingSource.ifEmpty { "employment_income" })
        )

        return AccountRequestDTO(
            agreements = emptyList(),
            contact = createContactFromState(state),
            disclosures = createDefaultDisclosures(),
            documents = emptyList(),
            enabledAssets = emptyList(),
            identity = identity,
            trustedContact = createEmptyTrustedContact()
        )
    }


    private fun buildContactRequestDTO(state: SignUpState): AccountRequestDTO {
        val contact = Contact(
            emailAddress = state.email,
            phoneNumber = state.phoneNumber,
            streetAddress = listOf(state.streetAddress),
            city = state.city,
            state = state.state,
            postalCode = state.postalCode,
            unit = ""
        )

        return AccountRequestDTO(
            agreements = emptyList(),
            contact = contact,
            disclosures = createDefaultDisclosures(),
            documents = emptyList(),
            enabledAssets = emptyList(),
            identity = createEmptyIdentity(),
            trustedContact = createEmptyTrustedContact()
        )
    }
    private fun buildPreferencesRequestDTO(state: SignUpState): AccountRequestDTO {
        val identity = Identity(
            givenName = state.firstName,
            familyName = state.lastName,
            dateOfBirth = state.dateOfBirth,
            taxId = state.taxId.ifEmpty { "" },
            taxIdType = "USA_SSN",
            countryOfBirth = state.countryCode,
            countryOfCitizenship = state.countryCode,
            countryOfTaxResidence = state.countryCode,
            fundingSource = listOf(state.fundingSource)
        )

        return AccountRequestDTO(
            agreements = emptyList(),
            contact = createContactFromState(state),
            disclosures = createDefaultDisclosures(),
            documents = emptyList(),
            enabledAssets = emptyList(),
            identity = identity,
            trustedContact = createEmptyTrustedContact()
        )
    }

    private fun buildAgreementsRequestDTO(state: SignUpState): AccountRequestDTO {
        return AccountRequestDTO(
            agreements = buildAgreementsList(state),
            contact = createContactFromState(state),
            disclosures = createDefaultDisclosures(),
            documents = emptyList(),
            enabledAssets = emptyList(),
            identity = createIdentityFromState(state),
            trustedContact = createEmptyTrustedContact()
        )
    }
    private fun buildTrustedContactRequestDTO(state: SignUpState): AccountRequestDTO {
        val trustedContact = if (state.hasTrustedContact) {
            TrustedContact(
                givenName = state.trustedContactName,
                familyName = "Doe",
                emailAddress = state.email
            )
        } else {
            createEmptyTrustedContact()
        }

        return AccountRequestDTO(
            agreements = buildAgreementsList(state),
            contact = createContactFromState(state),
            disclosures = createDefaultDisclosures(),
            documents = emptyList(),
            enabledAssets = emptyList(),
            identity = createIdentityFromState(state),
            trustedContact = trustedContact
        )
    }

    private fun buildDisclosuresRequestDTO(state: SignUpState): AccountRequestDTO {
        val disclosures = Disclosures(
            isControlPerson = state.isControlPerson,
            isAffiliatedExchangeOrFinra = state.isAffiliatedExchange,
            isPoliticallyExposed = state.isPoliticallyExposed,
            immediateFamilyExposed = state.immediateFamilyExposed
        )

        return AccountRequestDTO(
            agreements = buildAgreementsList(state),
            contact = createContactFromState(state),
            disclosures = disclosures,
            documents = emptyList(),
            enabledAssets = emptyList(),
            identity = createIdentityFromState(state),
            trustedContact = createTrustedContactFromState(state)
        )
    }
    private fun buildDocumentsRequestDTO(state: SignUpState): AccountRequestDTO {
        val documents = state.uploadedDocuments.map { documentId ->
            Document(
                documentType = "identity_verification",
                documentSubType = "passport",
                content = documentId,
                mimeType = "image/jpeg"
            )
        }

        return AccountRequestDTO(
            agreements = buildAgreementsList(state),
            contact = createContactFromState(state),
            disclosures = createDisclosuresFromState(state),
            documents = documents,
            enabledAssets = emptyList(),
            identity = createIdentityFromState(state),
            trustedContact = createTrustedContactFromState(state)
        )
    }
    private fun buildAgreementsList(state: SignUpState): List<Agreement> {
        val agreements = mutableListOf<Agreement>()

        if (state.accountAgreed) {
            agreements.add(
                Agreement(
                    agreement = "account_agreement",
                    signedAt = getCurrentTimestamp(),
                    ipAddress = "127.0.0.1",
                    revision = ""
                )
            )
        }

        if (state.customerAgreed) {
            agreements.add(
                Agreement(
                    agreement = "customer_agreement",
                    signedAt = getCurrentTimestamp(),
                    ipAddress = "127.0.0.1",
                    revision = ""
                )
            )
        }

        if (state.marginAgreed) {
            agreements.add(
                Agreement(
                    agreement = "margin_agreement",
                    signedAt = getCurrentTimestamp(),
                    ipAddress = "127.0.0.1",
                    revision = ""
                )
            )
        }

        if (state.optionsAgreed) {
            agreements.add(
                Agreement(
                    agreement = "options_agreement",
                    signedAt = getCurrentTimestamp(),
                    ipAddress = "127.0.0.1",
                    revision = ""
                )
            )
        }

        return agreements
    }

    private fun getCurrentTimestamp(): String {
        val timestamp = Date().time
        return java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", java.util.Locale.US)
            .format(Date(timestamp))
    }

    private fun createIdentityFromState(state: SignUpState): Identity {
        return Identity(
            givenName = state.firstName,
            familyName = state.lastName,
            dateOfBirth = state.dateOfBirth,
            taxId = state.taxId.ifEmpty { "" },
            taxIdType = "USA_SSN",
            countryOfBirth = state.countryCode,
            countryOfCitizenship = state.countryCode,
            countryOfTaxResidence = state.countryCode,
            fundingSource = listOf(state.fundingSource.ifEmpty { "employment_income" })
        )
    }

    private fun createContactFromState(state: SignUpState): Contact {
        return Contact(
            emailAddress = state.email,
            phoneNumber = state.phoneNumber,
            streetAddress = listOf(state.streetAddress.ifEmpty { "" }),
            city = state.city,
            state = state.state,
            postalCode = state.postalCode,
            unit = ""
        )
    }

    private fun createEmptyContact(): Contact {
        return Contact(
            emailAddress = "",
            phoneNumber = "",
            streetAddress = emptyList(),
            city = "",
            state = "",
            postalCode = "",
            unit = ""
        )
    }

    private fun createEmptyIdentity(): Identity {
        return Identity(
            givenName = "",
            familyName = "",
            dateOfBirth = "",
            taxId = "",
            taxIdType = "",
            countryOfBirth = "",
            countryOfCitizenship = "",
            countryOfTaxResidence = "",
            fundingSource = emptyList()
        )
    }

    private fun createDefaultDisclosures(): Disclosures {
        return Disclosures(
            isControlPerson = false,
            isAffiliatedExchangeOrFinra = false,
            isPoliticallyExposed = false,
            immediateFamilyExposed = false
        )
    }

    private fun createDisclosuresFromState(state: SignUpState): Disclosures {
        return Disclosures(
            isControlPerson = state.isControlPerson,
            isAffiliatedExchangeOrFinra = state.isAffiliatedExchange,
            isPoliticallyExposed = state.isPoliticallyExposed,
            immediateFamilyExposed = state.immediateFamilyExposed
        )
    }

    private fun createEmptyTrustedContact(): TrustedContact {
        return TrustedContact(
            givenName = "",
            familyName = "",
            emailAddress = ""
        )
    }

    private fun createTrustedContactFromState(state: SignUpState): TrustedContact {
        return if (state.hasTrustedContact) {
            TrustedContact(
                givenName = state.trustedContactName,
                familyName = "Doe",
                emailAddress = state.email
            )
        } else {
            TrustedContact(
                givenName = "",
                familyName = "",
                emailAddress = ""
            )
        }
    }
}