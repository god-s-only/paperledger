package com.paperledger.app.data.mappers.watchlists

import com.paperledger.app.data.local.WatchlistsEntity
import com.paperledger.app.data.remote.dto.watchlists_get.GetWatchlistsDTOItem

fun GetWatchlistsDTOItem.toDomain(): WatchlistsEntity{
    return WatchlistsEntity(
        id = id,
        name = name
    )
}