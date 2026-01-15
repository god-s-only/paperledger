package com.paperledger.app.core

fun alpacaAuthHeader(apiKey: String, apiSecret: String): String {
    val credentials = "$apiKey:$apiSecret"
    val base64 = android.util.Base64.encodeToString(credentials.toByteArray(), android.util.Base64.NO_WRAP)
    return "Basic $base64"
}