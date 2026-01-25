package com.paperledger.app.core

sealed class AppError(message: String? = null): Throwable() {
    object NetworkUnavailable : AppError("Network unavailable")
    data class HttpError(val code: Int, val reason: String? = null) : AppError("HTTP error: $code")
    object EmptyBody : AppError("Empty response body")
    data class Unknown(val reason: String?) : AppError(reason)
}