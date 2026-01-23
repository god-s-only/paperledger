package com.paperledger.app.presentation.ui.features.auth.signup

import android.util.Log
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
                    Log.d("AccountID", accountId)
                },
                onFailure = { error ->
                    _state.update { it.copy(isLoading = false, error = mapErrorMessage(error)) }
                }
            )
        }
    }

    fun onEvent(event: SignUpEvent) {
        when (event) {
            SignUpEvent.OnNextFromContactPage -> {
                if (isContactPageValid()) {
                    _state.value = _state.value.copy(
                        currentPage = 2,
                        error = null
                    )
                } else {
                    _state.value = _state.value.copy(
                        error = "Please complete all required contact fields"
                    )
                }
            }

            SignUpEvent.OnNextFromIdentityPage -> {
                if (isIdentityPageValid()) {
                    _state.value = _state.value.copy(
                        currentPage = 3,
                        error = null
                    )
                } else {
                    _state.value = _state.value.copy(
                        error = "Please complete all required identity fields"
                    )
                }
            }

            SignUpEvent.OnNextFromDisclosuresPage -> {
                _state.value = _state.value.copy(
                    currentPage = 4,
                    error = null
                )
            }

            SignUpEvent.OnNextFromDocumentsPage -> {
                if (_state.value.uploadedDocuments.isNotEmpty()) {
                    _state.value = _state.value.copy(
                        currentPage = 5,
                        error = null
                    )
                } else {
                    _state.value = _state.value.copy(
                        error = "Please upload at least one document"
                    )
                }
            }

            SignUpEvent.OnSubmitFromTrustedContactPage -> {
                viewModelScope.launch {
                    _state.value = _state.value.copy(isLoading = true, error = null)
                    val completeRequest = buildCompleteAccountRequest(_state.value)
                    signUp(completeRequest)
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

            is SignUpEvent.OnTrustedContactEmailChange -> {
                _state.value = _state.value.copy(trustedContactEmail = event.email)
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

    private fun buildCompleteAccountRequest(state: SignUpState): AccountRequestDTO {
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

        val contact = Contact(
            emailAddress = state.email,
            phoneNumber = state.phoneNumber,
            streetAddress = state.streetAddress,
            city = state.city,
            state = state.state,
            postalCode = state.postalCode,
        )

        val disclosures = Disclosures(
            isControlPerson = state.isControlPerson,
            isAffiliatedExchangeOrFinra = state.isAffiliatedExchange,
            isPoliticallyExposed = state.isPoliticallyExposed,
            immediateFamilyExposed = state.immediateFamilyExposed
        )

        val documents = state.uploadedDocuments.map { documentId ->
            Document(
                documentType = "identity_verification",
                documentSubType = "passport",
                content = documentId,
                mimeType = "image/jpeg"
            )
        }

        val trustedContact = if (state.hasTrustedContact) {
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

        val agreements = buildAgreementsList(state)

        return AccountRequestDTO(
            agreements = agreements,
            contact = contact,
            disclosures = disclosures,
            documents = documents,
            enabledAssets = state.enabledAssets,
            identity = identity,
            trustedContact = trustedContact
        )
    }
    private fun buildAgreementsList(state: SignUpState): List<Agreement> {
        val agreements = mutableListOf<Agreement>()
        agreements.add(
            Agreement(
                agreement = "customer_agreement",
                signedAt = getCurrentTimestamp(),
                ipAddress = "127.0.0.1",
            )
        )

        return agreements
    }

    private fun getCurrentTimestamp(): String {
        val timestamp = Date().time
        return java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", java.util.Locale.US)
            .format(Date(timestamp))
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