package com.paperledger.app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.paperledger.app.domain.models.watchlists.WatchlistsEntity

@Database(entities = [WatchlistsEntity::class], version = 1)
abstract class PaperledgerDatabase: RoomDatabase() {
    abstract fun paperledgerDao(): PaperledgerDAO
}