package com.socketChat.app.data.local
import androidx.room.*

@Dao
interface ChatMessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessage(msg: ChatMessageEntity)

    @Query("SELECT * FROM chat_messages WHERE botName = :bot ORDER BY timestamp ASC")
    suspend fun getMessagesForBot(bot: String): List<ChatMessageEntity>

    @Query("DELETE FROM chat_messages WHERE botName = :bot")
    suspend fun deleteHistory(bot: String)

    @Query("SELECT * FROM chat_messages WHERE botName = :bot ORDER BY timestamp DESC LIMIT 1")
    suspend fun getLatestMessageForBot(bot: String): ChatMessageEntity?

}
