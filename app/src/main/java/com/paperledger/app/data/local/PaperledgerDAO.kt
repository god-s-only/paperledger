package com.paperledger.app.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.paperledger.app.data.local.WatchlistsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PaperledgerDAO {
    @Insert
    suspend fun insertWatchlist(watchlistsEntity: List<WatchlistsEntity>)

    @Query("DELETE FROM watchlists")
    suspend fun deleteAllWatchlists()

    @Query("SELECT * FROM watchlists")
    fun observeAllWatchlists(): Flow<List<WatchlistsEntity>>

    @Transaction
    suspend fun replaceAllWatchlists(watchlistsEntity: List<WatchlistsEntity>) {
        deleteAllWatchlists()
        insertWatchlist(watchlistsEntity)
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPosition(positionEntity: List<PositionEntity>)

    @Query("DELETE FROM positions")
    suspend fun deleteAllPositions()

    @Query("SELECT * FROM positions")
    fun observeAllPositions(): Flow<List<PositionEntity>>

    @Transaction
    suspend fun replaceAllPositions(positionEntity: List<PositionEntity>){
        deleteAllPositions()
        insertPosition(positionEntity)
    }
}
