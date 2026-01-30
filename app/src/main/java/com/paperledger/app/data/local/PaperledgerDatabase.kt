package com.paperledger.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WatchlistsEntity::class], version = 1)
abstract class PaperledgerDatabase: RoomDatabase() {
    abstract fun paperledgerDao(): PaperledgerDAO
}