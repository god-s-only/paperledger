package com.paperledger.app.domain.models.watchlists

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watchlists")
data class WatchlistsEntity(
    @PrimaryKey(autoGenerate = false)
    val id: String = "",
    val name: String
)