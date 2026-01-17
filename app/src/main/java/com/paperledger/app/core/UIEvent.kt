package com.paperledger.app.core

sealed class UIEvent {
    data class Navigate(val route: String): UIEvent()
    data class ShowSnackBar(val message: String, val action: String? = null): UIEvent()
    object PopBackStack: UIEvent()
}