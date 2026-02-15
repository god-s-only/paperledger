package com.paperledger.app.presentation.ui.features.trade

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

@HiltViewModel
class PlaceTradeViewModel @Inject constructor(savedStateHandle: SavedStateHandle): ViewModel() {
    private val _state = MutableStateFlow(PlaceTradeState())
    val state = _state.asStateFlow()

    init {
        savedStateHandle.get<String>("watchlistName")?.let {
            _state.value = _state.value.copy(symbol = it)
        }
    }
}