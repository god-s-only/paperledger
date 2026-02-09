package com.paperledger.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "positions")
data class PositionEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String = "",
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
