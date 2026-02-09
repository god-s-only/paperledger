package com.paperledger.app.data.mappers.trade

import com.paperledger.app.data.local.OrderEntity
import com.paperledger.app.domain.models.trade.Order
import com.paperledger.app.data.remote.dto.pending_orders_get.GetPendingOrdersResponseDTOItem

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
        filledAt = filledAt,
        expiredAt = expiredAt,
        canceledAt = canceledAt,
        quantity = qty.toDouble(),
        filledQty = filledQty.toDouble(),
        limitPrice = limitPrice.toDouble(),
        commission = commission.toDouble(),
        assetClass = assetClass
    )
}

fun GetPendingOrdersResponseDTOItem.toEntity(): OrderEntity{
    return OrderEntity(
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
        filledAt = filledAt,
        expiredAt = expiredAt,
        canceledAt = canceledAt,
        quantity = qty.toDouble(),
        filledQty = filledQty.toDouble(),
        limitPrice = limitPrice.toDouble(),
        commission = commission.toDouble(),
        assetClass = assetClass
    )
}