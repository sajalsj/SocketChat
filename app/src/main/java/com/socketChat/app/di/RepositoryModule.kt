package com.socketChat.app.di

import android.content.Context
import com.socketChat.app.data.repository.ChatRepositoryImpl
import com.socketChat.app.domain.repository.ChatRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideChatRepository(
        @ApplicationContext context: Context
    ): ChatRepository {
        return ChatRepositoryImpl(context)
    }
}
