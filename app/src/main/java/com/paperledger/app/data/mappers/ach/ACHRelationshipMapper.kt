package com.paperledger.app.data.mappers.ach

import com.paperledger.app.data.remote.dto.ach_get.GetACHRelationshipsDTOItem
import com.paperledger.app.domain.models.ach.ACHRelationship

fun GetACHRelationshipsDTOItem.toACHRelationship(): ACHRelationship {
    return ACHRelationship(
        id = id,
        accountId = accountId,
        accountOwnerName = accountOwnerName,
        bankAccountNumber = bankAccountNumber,
        bankAccountType = bankAccountType,
        bankRoutingNumber = bankRoutingNumber,
        createdAt = createdAt,
        nickname = nickname,
        status = status,
        updatedAt = updatedAt
    )
}
