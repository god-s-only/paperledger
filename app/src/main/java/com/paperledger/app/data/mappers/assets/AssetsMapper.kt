package com.paperledger.app.data.mappers.assets

import com.paperledger.app.data.remote.dto.assets.AssetsResponseDTO
import com.paperledger.app.domain.models.assets.AssetsModel

fun AssetsResponseDTO.toAssetsDomain(): AssetsModel{
    return AssetsModel(
        symbol = this.first().symbol,
        name = this.first().name,
        exchange = this.first().exchange,
        status = this.first().status,
        assetClass = this.first().classX
    )
}