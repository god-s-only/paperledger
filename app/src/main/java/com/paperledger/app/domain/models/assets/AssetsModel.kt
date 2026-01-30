package com.paperledger.app.domain.models.assets

data class AssetsModel(
    val id: String,
    val symbol: String,
    val name: String,
    val exchange: String,
    val status: String,
    val assetClass: String
)
