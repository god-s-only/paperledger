package com.paperledger.app.presentation.ui.features.assets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paperledger.app.core.UIEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AssetsViewModel @Inject constructor(): ViewModel() {
    private val _state = MutableStateFlow(AssetsState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {

    }
    fun onEvent(event: AssetsScreenEvent){
        when(event){
            is AssetsScreenEvent.OnAssetClick -> {

            }
            is AssetsScreenEvent.OnSearchQueryChange -> {
                _state.value = _state.value.copy(searchQuery = event.searchQuery)
            }
        }
    }
    private fun sendUIEvent(event: UIEvent){
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}