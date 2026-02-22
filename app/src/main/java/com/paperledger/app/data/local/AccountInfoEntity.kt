package com.paperledger.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "account_info")
data class AccountInfoEntity(
    @PrimaryKey(autoGenerate = false)
    val accountId: String = "",
    val currency: String,
    val lastEquity: Double,
    val createdAt: String,
    val status: String,
    val accountType: String
)