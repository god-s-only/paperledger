package com.paperledger.app.core

import okio.IOException
import retrofit2.HttpException

sealed class AppError(message: String? = null): Throwable() {
    object NetworkUnavailable : AppError("Network unavailable")
    data class HttpError(val code: Int, val reason: String? = null) : AppError("HTTP error: $code")
    object EmptyBody : AppError("Empty response body")
    data class Unknown(val reason: String?) : AppError(reason)
}
 fun mapError(e: Exception): AppError{
    return when(e){
        is IOException -> AppError.NetworkUnavailable
        is HttpException -> AppError.HttpError(e.code(), e.message)
        else -> AppError.Unknown(e.message)
    }
}

 fun mapErrorMessage(error: Throwable): String {
    return when (error) {
        is AppError.HttpError -> {
            when (error.code) {
                400 -> "Bad Request: ${error.reason}"
                401 -> "Unauthorized: ${error.reason}"
                403 -> "Forbidden: ${error.reason}"
                409 -> "Conflict: ${error.reason}"
                422 -> "Invalid Input ${error.reason}"
                else -> error.message ?: "Unknown Error"
            }
        }
        is AppError.Unknown -> error.message ?: "An unknown error occurred"
        is AppError.EmptyBody -> "Response body is empty"
        is AppError.NetworkUnavailable -> "Network unavailable. Please check your connection"
        else -> error.message ?: "An error occurred"
    }
}