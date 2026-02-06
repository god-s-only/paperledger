package com.paperledger.app.data.mappers.trade

import com.paperledger.app.domain.models.trade.Order

fun GetPendingOrdersResponseDTOItem.toDomain(): Order{
    return Order(
        id = id,
        clientOrderId = clientOrderId,
        symbol = symbol,
        side = side,
        type = type,
        orderClass = orderClass,
        timeInForce = timeInForce,
        status = status,
        createdAt = createdAt,
        updatedAt = updatedAt,
        submittedAt = submittedAt,
        filledAt = filledAt.toString(),
        expiredAt = expiredAt.toString(),
        canceledAt = canceledAt.toString(),
        quantity = qty.toDouble(),
        filledQty = filledQty.toDouble(),
        limitPrice = limitPrice.toDouble(),
        commission = commission.toDouble(),
        assetClass = assetClass
    )
}