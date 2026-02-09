package com.paperledger.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.paperledger.app.data.local.WatchlistsEntity

@Database(entities = [WatchlistsEntity::class, PositionEntity::class, OrderEntity::class], version = 1)
abstract class PaperledgerDatabase: RoomDatabase() {
    abstract fun paperledgerDao(): PaperledgerDAO
}