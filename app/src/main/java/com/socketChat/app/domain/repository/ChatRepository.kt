package com.socketChat.app.domain.repository

import com.socketChat.app.domain.model.ChatModel
import com.socketChat.app.data.local.QueuedMessageEntity

interface ChatRepository {
    suspend fun sendMessage(message: String): Boolean
    suspend fun queueMessage(message: String, bot: String)
    suspend fun getQueuedMessages(): List<QueuedMessageEntity>
    suspend fun removeQueuedMessage(message: QueuedMessageEntity)

    suspend fun saveMessageToHistory(message: String, bot: String, fromMe: Boolean)
    suspend fun getMessageHistory(bot: String): List<ChatModel>
    suspend fun getLatestMessage(bot: String): ChatModel?

}
