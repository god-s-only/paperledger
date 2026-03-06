package com.paperledger.app.data.mappers.funding

import com.paperledger.app.data.remote.dto.funding.response.success.FundingResponseDTO
import com.paperledger.app.domain.models.funding.Transfer

fun FundingResponseDTO.toTransfer(): Transfer {
    return Transfer(
        id = id,
        accountId = accountId,
        amount = amount,
        createdAt = createdAt,
        currency = currency,
        direction = direction,
        expiresAt = expiresAt,
        fee = fee,
        feePaymentMethod = feePaymentMethod,
        instantAmount = instantAmount,
        relationshipId = relationshipId,
        requestedAmount = requestedAmount,
        status = status,
        type = type,
        updatedAt = updatedAt
    )
}
