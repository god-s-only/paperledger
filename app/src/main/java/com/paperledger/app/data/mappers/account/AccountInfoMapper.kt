package com.paperledger.app.data.mappers.account

import com.paperledger.app.data.local.AccountInfoEntity
import com.paperledger.app.data.remote.dto.account.response.success.AccountResponseDTO
import com.paperledger.app.domain.models.trade.AccountInfo

fun AccountResponseDTO.toAccountInfo(): AccountInfo {
    return AccountInfo(
        accountId = id,
        currency = currency,
        lastEquity = lastEquity.toDouble(),
        createdAt = createdAt,
        status = status,
        accountType = accountType
    )
}

fun AccountResponseDTO.toEntity(): AccountInfoEntity {
    return AccountInfoEntity(
        accountId = id,
        currency = currency,
        lastEquity = lastEquity.toDouble(),
        createdAt = createdAt,
        status = status,
        accountType = accountType
    )
}

fun AccountInfoEntity.toDomain(): AccountInfo {
    return AccountInfo(
        accountId = accountId,
        currency = currency,
        lastEquity = lastEquity,
        createdAt = createdAt,
        status = status,
        accountType = accountType
    )
}