package com.paperledger.app.domain.models.trade

data class Position(
    val symbol: String,
    val quantity: Double,
    val side: String,
    val entryPrice: Double,
    val currentPrice: Double,
    val marketValue: Double,
    val costBasis: Double,
    val unrealizedPl: Double,
    val unrealizedPlPercent: Double,
    val unrealizedIntradayPl: Double,
    val unrealizedIntradayPlPercent: Double,
    val exchange: String,
    val assetClass: String
)