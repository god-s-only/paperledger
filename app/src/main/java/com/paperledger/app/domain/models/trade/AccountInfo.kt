package com.paperledger.app.domain.models.trade

data class AccountInfo(
    val accountId: String,
    val currency: String,
    val lastEquity: Double,
    val createdAt: String,
    val status: String,
    val accountType: String,
)