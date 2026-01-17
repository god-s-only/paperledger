package com.paperledger.app.presentation.ui.features.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paperledger.app.core.UIEvent
import com.paperledger.app.domain.usecase.auth.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(private val signUpUseCase: SignUpUseCase): ViewModel() {
    private val _state = MutableStateFlow(SignUpState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: SignUpEvent){
        when(event){
            is SignUpEvent.OnSubmit -> {
                if (isFormValid()) {
                    viewModelScope.launch {
                        _state.value = _state.value.copy(isLoading = true, error = null)
                        // Convert state to AccountRequestDTO and submit
                        // val accountRequestDTO = state.value.toAccountRequestDTO()
                        // val result = signUpUseCase(accountRequestDTO)
                        _state.value = _state.value.copy(isLoading = false, isSuccess = true)
                    }
                } else {
                    _state.value = _state.value.copy(error = "Please fill in all required fields")
                }
            }
            // Identity Information
            is SignUpEvent.OnFirstNameChange -> {
                _state.value = _state.value.copy(firstName = event.firstName)
            }

            is SignUpEvent.OnLastNameChange -> {
                _state.value = _state.value.copy(lastName = event.lastName)
            }

            is SignUpEvent.OnDateOfBirthChange -> {
                _state.value = _state.value.copy(dateOfBirth = event.dateOfBirth)
            }

            is SignUpEvent.OnTaxIdChange -> {
                _state.value = _state.value.copy(taxId = event.taxId)
            }

            is SignUpEvent.OnCountryCodeChange -> {
                _state.value = _state.value.copy(countryCode = event.countryCode)
            }
            // Contact Information
            is SignUpEvent.OnEmailChange -> {
                _state.value = _state.value.copy(email = event.email, error = null)
            }

            is SignUpEvent.OnPhoneNumberChange -> {
                _state.value = _state.value.copy(phoneNumber = event.phoneNumber)
            }

            is SignUpEvent.OnStreetAddressChange -> {
                _state.value = _state.value.copy(streetAddress = event.streetAddress)
            }

            is SignUpEvent.OnCityChange -> {
                _state.value = _state.value.copy(city = event.city)
            }

            is SignUpEvent.OnStateChange -> {
                _state.value = _state.value.copy(state = event.state)
            }

            is SignUpEvent.OnPostalCodeChange -> {
                _state.value = _state.value.copy(postalCode = event.postalCode)
            }
            // Account Preferences
            is SignUpEvent.OnAccountTypeChange -> {
                _state.value = _state.value.copy(accountType = event.accountType)
            }

            is SignUpEvent.OnFundingSourceChange -> {
                _state.value = _state.value.copy(fundingSource = event.fundingSource)
            }
            // Agreement Terms
            is SignUpEvent.OnAccountAgreementChange -> {
                _state.value = _state.value.copy(accountAgreed = event.agreed, error = null)
            }

            is SignUpEvent.OnCustomerAgreementChange -> {
                _state.value = _state.value.copy(customerAgreed = event.agreed, error = null)
            }

            is SignUpEvent.OnMarginAgreementChange -> {
                _state.value = _state.value.copy(marginAgreed = event.agreed, error = null)
            }

            is SignUpEvent.OnOptionsAgreementChange -> {
                _state.value = _state.value.copy(optionsAgreed = event.agreed, error = null)
            }
            // Trusted Contact Information
            is SignUpEvent.OnTrustedContactGivenChange -> {
                _state.value = _state.value.copy(hasTrustedContact = event.hasTrustedContact)
            }

            is SignUpEvent.OnTrustedContactNameChange -> {
                _state.value = _state.value.copy(trustedContactName = event.name)
            }

            is SignUpEvent.OnTrustedContactPhoneChange -> {
                _state.value = _state.value.copy(trustedContactPhone = event.phone)
            }
            // Disclosure Information
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
            // Document Uploads
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
            // Form Actions
            SignUpEvent.OnFormReset -> {
                _state.value = SignUpState()
            }

            SignUpEvent.OnNavigateBack -> {
                _state.value = _state.value.copy(error = null)
            }
        }
    }

    private fun isFormValid(): Boolean {
        val state = _state.value
        return state.firstName.isNotBlank() &&
                state.lastName.isNotBlank() &&
                state.dateOfBirth.isNotBlank() &&
                state.email.isNotBlank() &&
                state.phoneNumber.isNotBlank() &&
                state.streetAddress.isNotBlank() &&
                state.city.isNotBlank() &&
                state.state.isNotBlank() &&
                state.postalCode.isNotBlank() &&
                state.accountAgreed &&
                state.customerAgreed
    }
    private fun sendUIEvent(event: UIEvent){
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}