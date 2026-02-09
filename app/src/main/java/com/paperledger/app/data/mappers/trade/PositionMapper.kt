package com.paperledger.app.data.mappers.trade

import com.paperledger.app.data.local.PositionEntity
import com.paperledger.app.data.remote.dto.open_positions_get.GetOpenPositionResponseDTOItem
import com.paperledger.app.domain.models.trade.Position

fun GetOpenPositionResponseDTOItem.toDomain(): Position{
    return Position(
        symbol = symbol,
        quantity = qty.toDouble(),
        side = side,
        entryPrice = avgEntryPrice.toDouble(),
        currentPrice = currentPrice.toDouble(),
        marketValue = marketValue.toDouble(),
        costBasis = costBasis.toDouble(),
        unrealizedPl = unrealizedPl.toDouble(),
        unrealizedPlPercent = unrealizedPlpc.toDouble(),
        unrealizedIntradayPl = unrealizedIntradayPl.toDouble(),
        unrealizedIntradayPlPercent = unrealizedIntradayPlpc.toDouble(),
        exchange = exchange,
        assetClass = assetClass
    )
}
fun GetOpenPositionResponseDTOItem.toEntity(): PositionEntity{
    return PositionEntity(
        symbol = symbol,
        quantity = qty.toDouble(),
        side = side,
        entryPrice = avgEntryPrice.toDouble(),
        currentPrice = currentPrice.toDouble(),
        marketValue = marketValue.toDouble(),
        costBasis = costBasis.toDouble(),
        unrealizedPl = unrealizedPl.toDouble(),
        unrealizedPlPercent = unrealizedPlpc.toDouble(),
        unrealizedIntradayPl = unrealizedIntradayPl.toDouble(),
        unrealizedIntradayPlPercent = unrealizedIntradayPlpc.toDouble(),
        exchange = exchange,
        assetClass = assetClass
    )
}