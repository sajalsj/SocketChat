package com.socketChat.app.data.local
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [QueuedMessageEntity::class, ChatMessageEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun queuedMessageDao(): QueuedMessageDao
    abstract fun chatMessageDao(): ChatMessageDao
    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "chat_app_db"
                ).build().also { INSTANCE = it }
            }
        }
    }
}

