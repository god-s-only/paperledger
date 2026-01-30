package com.paperledger.app.presentation.ui.features.assets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paperledger.app.core.UIEvent
import com.paperledger.app.core.mapErrorMessage
import com.paperledger.app.domain.usecase.assets.GetAllAssetsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AssetsViewModel @Inject constructor(private val getAllAssetsUseCase: GetAllAssetsUseCase): ViewModel() {
    private val _state = MutableStateFlow(AssetsState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    error = null
                )
            }
            getAllAssetsUseCase.invoke().fold(
                onSuccess = { assets ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = null,
                            assets = assets
                        )
                    }
                },
                onFailure = { e ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            error = mapErrorMessage(e),
                            assets = emptyList()
                        )
                    }
                }
            )
        }

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