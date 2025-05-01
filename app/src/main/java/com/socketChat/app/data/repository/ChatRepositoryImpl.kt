package com.socketChat.app.data.repository
import android.content.Context
import com.socketChat.app.domain.model.ChatModel
import com.socketChat.app.data.local.AppDatabase
import com.socketChat.app.data.local.ChatMessageEntity
import com.socketChat.app.data.local.QueuedMessageEntity
import com.socketChat.app.domain.repository.ChatRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.WebSocket

class ChatRepositoryImpl(
    context: Context,
) : ChatRepository {

    private val db = AppDatabase.getDatabase(context)
    private val queuedDao = db.queuedMessageDao()
    private val chatDao = db.chatMessageDao()
    private var webSocket: WebSocket? = null


    override suspend fun sendMessage(message: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                webSocket?.send(message) ?: false
            } catch (e: Exception) {
                false
            }
        }
    }

    fun setWebSocket(socket: WebSocket) {
        this.webSocket = socket
    }

    override suspend fun queueMessage(message: String, bot: String) {
        withContext(Dispatchers.IO) {
            queuedDao.insert(
                QueuedMessageEntity(message = message, botName = bot)
            )
        }
    }

    override suspend fun getQueuedMessages(): List<QueuedMessageEntity> {
        return withContext(Dispatchers.IO) {
            queuedDao.getAll()
        }
    }

    override suspend fun removeQueuedMessage(message: QueuedMessageEntity) {
        withContext(Dispatchers.IO) {
            queuedDao.delete(message)
        }
    }

    override suspend fun saveMessageToHistory(message: String, bot: String, fromMe: Boolean) {
        withContext(Dispatchers.IO) {
            chatDao.insertMessage(
                ChatMessageEntity(
                    botName = bot,
                    message = message,
                    fromMe = fromMe
                )
            )
        }
    }

    override suspend fun getMessageHistory(bot: String): List<ChatModel> {
        return withContext(Dispatchers.IO) {
            chatDao.getMessagesForBot(bot).map {
                ChatModel(it.message, it.fromMe)
            }
        }
    }

    override suspend fun getLatestMessage(bot: String): ChatModel? {
        return withContext(Dispatchers.IO) {
            chatDao.getLatestMessageForBot(bot)?.let {
                ChatModel(it.message, it.fromMe)
            }
        }
    }

}
