package com.paperledger.app.presentation.ui.features.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paperledger.app.data.local.PaperLedgerSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val session: PaperLedgerSession
) : ViewModel() {

    val isDarkMode = session.darkModeFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = true
    )

    fun toggleDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            session.setDarkMode(enabled)
        }
    }
}