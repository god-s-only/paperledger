package com.paperledger.app.presentation.ui.features.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paperledger.app.core.Routes
import com.paperledger.app.core.UIEvent
import com.paperledger.app.data.local.PaperLedgerSession
import com.paperledger.app.domain.usecase.auth.LogoutUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val session: PaperLedgerSession,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {

    val isDarkMode = session.darkModeFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = true
    )

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun toggleDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            session.setDarkMode(enabled)
        }
    }

    fun logout() {
        viewModelScope.launch {
            logoutUseCase()
            _uiEvent.send(UIEvent.Navigate(Routes.ONBOARDING_SCREEN))
        }
    }
}