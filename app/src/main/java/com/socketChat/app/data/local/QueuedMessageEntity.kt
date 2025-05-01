package com.socketChat.app.data.local
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "queued_messages")
data class QueuedMessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val message: String,
    val botName: String,
    val timestamp: Long = System.currentTimeMillis()
)
