package com.paperledger.app.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watchlists")
data class WatchlistsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0
)