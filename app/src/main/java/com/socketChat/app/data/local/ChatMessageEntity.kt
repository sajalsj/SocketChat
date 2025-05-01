package com.socketChat.app.data.local
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "chat_messages")
data class ChatMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val botName: String,
    val message: String,
    val fromMe: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)
