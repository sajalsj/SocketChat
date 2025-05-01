package com.socketChat.app.data.repository
import android.content.Context
import com.socketChat.app.data.local.ChatMessageEntity
import com.socketChat.app.data.local.QueuedMessageEntity
import com.socketChat.app.data.local.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MessageRepository(context: Context) {

    private val dao = AppDatabase.getDatabase(context).queuedMessageDao()
    private val chatDao = AppDatabase.getDatabase(context).chatMessageDao()

    suspend fun saveChatMessage(bot: String, message: String, fromMe: Boolean) {
        chatDao.insertMessage(ChatMessageEntity(botName = bot, message = message, fromMe = fromMe))
    }

    suspend fun getChatHistory(bot: String): List<ChatMessageEntity> {
        return chatDao.getMessagesForBot(bot)
    }

    suspend fun saveToQueue(message: String, bot: String) {
        withContext(Dispatchers.IO) {
            dao.insert(QueuedMessageEntity(message = message, botName = bot))
        }
    }

    suspend fun getQueuedMessages(): List<QueuedMessageEntity> {
        return withContext(Dispatchers.IO) {
            dao.getAll()
        }
    }

    suspend fun removeMessage(msg: QueuedMessageEntity) {
        withContext(Dispatchers.IO) {
            dao.delete(msg)
        }
    }

    suspend fun clearAll() {
        withContext(Dispatchers.IO) {
            dao.clearAll()
        }
    }
}
