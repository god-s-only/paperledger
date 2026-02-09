package com.paperledger.app.data.mappers.trade

import com.paperledger.app.data.local.PositionEntity
import com.paperledger.app.data.remote.dto.open_positions_get.GetOpenPositionResponseDTOItem
import com.paperledger.app.domain.models.trade.Position

fun GetOpenPositionResponseDTOItem.toEntity(): PositionEntity{
    return PositionEntity(
        id = assetId,
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
fun PositionEntity.toDomain(): Position{
    return Position(
        id = id,
        symbol = symbol,
        quantity = quantity,
        side = side,
        entryPrice = entryPrice,
        currentPrice = currentPrice,
        marketValue = marketValue,
        costBasis = costBasis,
        unrealizedPl = unrealizedPl,
        unrealizedPlPercent = unrealizedPlPercent,
        unrealizedIntradayPl = unrealizedIntradayPl,
        unrealizedIntradayPlPercent = unrealizedIntradayPlPercent,
        exchange = exchange,
        assetClass = assetClass
    )
}

fun GetOpenPositionResponseDTOItem.toDomain(): Position{
    return Position(
        id = assetId,
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