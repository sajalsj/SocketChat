package com.socketChat.app.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socketChat.app.domain.model.ConversationModel
import com.socketChat.app.domain.repository.ChatRepository
import kotlinx.coroutines.launch

class ConversationViewModel(
    private val repository: ChatRepository

): ViewModel() {

    private val _conversations = MutableLiveData<List<ConversationModel>>()
    val conversations: LiveData<List<ConversationModel>> = _conversations

    init {
        viewModelScope.launch {
            val bots = listOf("SupportBot", "SalesBot", "FAQBot")

            val conversationList = bots.map { botName ->
                val last = repository.getLatestMessage(botName)
                ConversationModel(
                    botName = botName,
                    lastMessage = last?.msg ?: "Say hi to $botName!",
                    unreadCount = 0
                )
            }
            _conversations.postValue(conversationList)
        }
    }

    fun getLatestMessage() {
        viewModelScope.launch {
            val bots = listOf("SupportBot", "SalesBot", "FAQBot")

            val conversationList = bots.map { botName ->
                val last = repository.getLatestMessage(botName)
                ConversationModel(
                    botName = botName,
                    lastMessage = last?.msg ?: "Say hi to $botName!",
                    unreadCount = 0
                )
            }
            _conversations.postValue(conversationList)
        }
    }


    fun updateConversation(botName: String, lastMessage: String) {
        _conversations.value = _conversations.value?.map {
            if (it.botName == botName) {
                it.copy(lastMessage = lastMessage, unreadCount = it.unreadCount + 1)
            } else it
        }
    }

    fun markAsRead(botName: String) {
        _conversations.value = _conversations.value?.map {
            if (it.botName == botName) {
                it.copy(unreadCount = 0)
            } else it
        }
    }
}
