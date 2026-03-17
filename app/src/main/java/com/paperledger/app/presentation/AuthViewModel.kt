package com.paperledger.app.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paperledger.app.domain.usecase.ach.GetACHRelationshipTokenUseCase
import com.paperledger.app.domain.usecase.auth.GetUserIdUseCase
import com.paperledger.app.domain.usecase.funding.GetFundingTokenUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class AuthDestination {
    object Loading : AuthDestination()
    object SignUp : AuthDestination()
    object ACHRelationship : AuthDestination()
    object Funding : AuthDestination()
    object Watchlists : AuthDestination()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val getUserIdUseCase: GetUserIdUseCase,
    private val getACHRelationshipTokenUseCase: GetACHRelationshipTokenUseCase,
    private val getFundingTokenUseCase: GetFundingTokenUseCase
) : ViewModel() {

    private val _authDestination = MutableStateFlow<AuthDestination>(AuthDestination.Loading)
    val authDestination: StateFlow<AuthDestination> = _authDestination.asStateFlow()

    init {
        resolveStartDestination()
    }

    fun resolveStartDestination() {
        viewModelScope.launch {
            val userId = getUserIdUseCase()
            if (userId == null) {
                _authDestination.value = AuthDestination.SignUp
                return@launch
            }

            val achToken = getACHRelationshipTokenUseCase()
            if (achToken == null) {
                _authDestination.value = AuthDestination.ACHRelationship
                return@launch
            }

            val fundingToken = getFundingTokenUseCase()
            if (fundingToken == null) {
                _authDestination.value = AuthDestination.Funding
                return@launch
            }
            _authDestination.value = AuthDestination.Watchlists
        }
    }
}