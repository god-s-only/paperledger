package com.paperledger.app.presentation.ui.features.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paperledger.app.core.AppError
import com.paperledger.app.core.UIEvent
import com.paperledger.app.data.remote.dto.account.request.*
import com.paperledger.app.domain.usecase.auth.GetUserIdUseCase
import com.paperledger.app.domain.usecase.auth.SignUpUseCase
import com.paperledger.app.domain.usecase.auth.StoreUserIdUseCase
import com.paperledger.app.domain.usecase.auth.UpdateAccountUseCase
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
    private val storeUserIdUseCase: StoreUserIdUseCase,
    private val updateAccountUseCase: UpdateAccountUseCase,
    private val getUserIdUseCase: GetUserIdUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(SignUpState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()
    
    fun signUp(contactRequest: AccountRequestDTO){
        viewModelScope.launch {
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
                    _state.update { it.copy(isLoading = false, error = mapErrorMessage(error)) }
                }
            )
        }
    }

    fun updateAccount(accountRequest: AccountRequestDTO){
        viewModelScope.launch {
            updateAccountUseCase.invoke(_state.value.accountId ?: "", accountRequest).fold(
                onSuccess = {
                    _state.update { 
                        it.copy(
                            isLoading = false
                        )
                    }
                },
                onFailure = { error ->
                    _state.update { 
                        it.copy(
                            isLoading = false,
                            error = mapErrorMessage(error)
                        )
                    }
                }
            )
        }
    }
    
    fun onEvent(event: SignUpEvent) {
        when (event) {
            SignUpEvent.OnNextFromContactPage -> {
                if (isContactPageValid()) {
                    _state.value = _state.value.copy(isLoading = true, error = null)
                    val contactRequest = buildContactRequestDTO(_state.value)
                    signUp(contactRequest)
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
                        updateAccountUseCase.invoke(_state.value.accountId ?: "", identityRequest)
                            .fold(
                                onSuccess = {
                                    _state.value =
                                        _state.value.copy(currentPage = 3, isLoading = false)
                                },
                                onFailure = { error ->
                                    _state.update {
                                        it.copy(
                                            isLoading = false,
                                            error = mapErrorMessage(error)
                                        )
                                    }
                                }
                            )
                    }
                } else {
                    _state.value =
                        _state.value.copy(error = "Please complete all required identity fields")
                }
            }

            SignUpEvent.OnNextFromDisclosuresPage -> {
                viewModelScope.launch {
                    _state.value = _state.value.copy(isLoading = true, error = null)
                    val disclosuresRequest = buildDisclosuresRequestDTO(_state.value)
                    updateAccountUseCase.invoke(_state.value.accountId ?: "", disclosuresRequest)
                        .fold(
                            onSuccess = {
                                _state.value = _state.value.copy(currentPage = 4, isLoading = false)
                            },
                            onFailure = { error ->
                                _state.update {
                                    it.copy(
                                        isLoading = false,
                                        error = mapErrorMessage(error)
                                    )
                                }
                            }
                        )
                }
            }

            SignUpEvent.OnNextFromDocumentsPage -> {
                if (state.value.uploadedDocuments.isNotEmpty()) {
                    viewModelScope.launch {
                        _state.value = _state.value.copy(isLoading = true, error = null)
                        val documentsRequest = buildDocumentsRequestDTO(_state.value)
                        updateAccountUseCase.invoke(_state.value.accountId ?: "", documentsRequest)
                            .fold(
                                onSuccess = {
                                    _state.value =
                                        _state.value.copy(currentPage = 5, isLoading = false)
                                },
                                onFailure = { error ->
                                    _state.update {
                                        it.copy(
                                            isLoading = false,
                                            error = mapErrorMessage(error)
                                        )
                                    }
                                }
                            )
                    }
                } else {
                    _state.value = _state.value.copy(error = "Please upload your identity document")
                }
            }

            SignUpEvent.OnSubmitFromTrustedContactPage -> {
                viewModelScope.launch {
                    _state.value = _state.value.copy(isLoading = true, error = null)
                    val trustedContactRequest = buildTrustedContactRequestDTO(_state.value)
                    updateAccountUseCase.invoke(_state.value.accountId ?: "", trustedContactRequest)
                        .fold(
                            onSuccess = {
                                _state.value =
                                    _state.value.copy(isLoading = false, isSuccess = true)
                            },
                            onFailure = { error ->
                                _state.update {
                                    it.copy(
                                        isLoading = false,
                                        error = mapErrorMessage(error)
                                    )
                                }
                            }
                        )
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
                if (_state.value.currentPage < 5) {
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
                updateCanNavigateNext()
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

            is SignUpEvent.OnUnitChange -> {
                _state.value = _state.value.copy(unit = event.unit)
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

            is SignUpEvent.OnFundingSourcesChange -> {
                _state.value = _state.value.copy(fundingSource = event.fundingSources)
                updateCanNavigateNext()
            }

            is SignUpEvent.OnEnabledAssetsChange -> {
                _state.value = _state.value.copy(enabledAssets = event.enabledAssets)
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
                state.countryCode.isNotBlank() &&
                state.fundingSource.isNotEmpty() &&
                state.enabledAssets.isNotEmpty()
    }

    private fun isContactPageValid(): Boolean {
        val state = _state.value
        return state.email.isNotBlank() &&
                state.phoneNumber.isNotBlank() &&
                state.streetAddress.isNotEmpty() &&
                state.city.isNotBlank() &&
                state.state.isNotBlank() &&
                state.postalCode.isNotBlank()
    }

    private fun updateCanNavigateNext() {
        val current = _state.value
        val canNavigate = when (current.currentPage) {
            1 -> isContactPageValid()
            2 -> isIdentityPageValid()
            3 -> true
            4 -> current.uploadedDocuments.isNotEmpty()
            5 -> true
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
            fundingSource = state.fundingSource
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
            streetAddress = state.streetAddress,
            city = state.city,
            state = state.state,
            postalCode = state.postalCode,
            unit = state.unit
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
            enabledAssets = state.enabledAssets,
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
            enabledAssets = state.enabledAssets,
            identity = createIdentityFromState(state),
            trustedContact = createTrustedContactFromState(state)
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
            disclosures = createDisclosuresFromState(state),
            documents = emptyList(),
            enabledAssets = state.enabledAssets,
            identity = createIdentityFromState(state),
            trustedContact = trustedContact
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
            fundingSource = state.fundingSource
        )
    }

    private fun createContactFromState(state: SignUpState): Contact {
        return Contact(
            emailAddress = state.email,
            phoneNumber = state.phoneNumber,
            streetAddress = state.streetAddress.takeIf { it.isNotEmpty() } ?: emptyList(),
            city = state.city,
            state = state.state,
            postalCode = state.postalCode,
            unit = state.unit
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
    private fun mapErrorMessage(error: Throwable): String {
        return when (error) {
            is AppError.HttpError -> {
                when (error.code) {
                    400 -> "Bad Request"
                    409 -> "Account already exists"
                    422 -> "Invalid Input"
                    else -> error.message ?: "Unknown Error"
                }
            }
            is AppError.Unknown -> error.message ?: "An unknown error occurred"
            is AppError.EmptyBody -> "Response body is empty"
            is AppError.NetworkUnavailable -> "Network unavailable. Please check your connection"
            else -> error.message ?: "An error occurred"
        }
    }
}