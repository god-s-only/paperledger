package com.paperledger.app.data.mappers.assets

import com.paperledger.app.data.remote.dto.assets.AssetsResponseDTO
import com.paperledger.app.data.remote.dto.assets.AssetsResponseDTOItem
import com.paperledger.app.domain.models.assets.AssetsModel

fun AssetsResponseDTOItem.toAssetsDomain(): AssetsModel{
    return AssetsModel(
        id = id,
        symbol = symbol,
        name = name,
        exchange = exchange,
        status = status,
        assetClass = classX
    )
}