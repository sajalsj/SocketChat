package com.socketChat.app.data.local
import androidx.room.*

@Dao
interface QueuedMessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(message: QueuedMessageEntity)

    @Query("SELECT * FROM queued_messages ORDER BY timestamp ASC")
    suspend fun getAll(): List<QueuedMessageEntity>

    @Delete
    suspend fun delete(message: QueuedMessageEntity)

    @Query("DELETE FROM queued_messages")
    suspend fun clearAll()
}
