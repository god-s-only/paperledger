package com.paperledger.app.presentation.ui.features.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paperledger.app.core.UIEvent
import com.paperledger.app.core.mapErrorMessage
import com.paperledger.app.domain.usecase.auth.GetProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileUseCase: GetProfileUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            getProfileUseCase().fold(
                onSuccess = { profile ->
                    _state.update { it.copy(isLoading = false, profile = profile) }
                },
                onFailure = { e ->
                    val message = mapErrorMessage(e)
                    _state.update { it.copy(isLoading = false, error = message) }
                    _uiEvent.send(UIEvent.ShowSnackBar(message = message))
                }
            )
        }
    }

    fun navigateBack() {
        viewModelScope.launch {
            _uiEvent.send(UIEvent.PopBackStack)
        }
    }
}