package com.paperledger.app.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
interface PaperledgerDAO {

    @Query("SELECT * FROM positions")
    fun observeAllPositions(): Flow<List<PositionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPositions(positions: List<PositionEntity>)

    @Query("DELETE FROM positions")
    suspend fun deleteAllPositions()

    @Transaction
    suspend fun replaceAllPositions(positions: List<PositionEntity>) {
        deleteAllPositions()
        insertPositions(positions)
    }

    @Query("SELECT * FROM orders")
    fun observeAllOrders(): Flow<List<OrderEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrders(orders: List<OrderEntity>)

    @Query("DELETE FROM orders")
    suspend fun deleteAllOrders()

    @Transaction
    suspend fun replaceAllOrders(orders: List<OrderEntity>) {
        deleteAllOrders()
        insertOrders(orders)
    }


    @Query("SELECT * FROM account_info LIMIT 1")
    fun observeAccountInfo(): Flow<AccountInfoEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccountInfo(accountInfo: AccountInfoEntity)

    @Query("DELETE FROM account_info")
    suspend fun deleteAccountInfo()

    @Transaction
    suspend fun replaceAccountInfo(accountInfo: AccountInfoEntity) {
        deleteAccountInfo()
        insertAccountInfo(accountInfo)
    }
}