package com.socketChat.app.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socketChat.app.domain.model.ConversationModel
import com.socketChat.app.domain.repository.ChatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConversationViewModel @Inject constructor(
    private val repository: ChatRepository
) : ViewModel() {

    private val _conversations = MutableStateFlow<List<ConversationModel>>(emptyList())
    val conversations: StateFlow<List<ConversationModel>> = _conversations

    init {
        getLatestMessage()
    }

    fun getLatestMessage() {
        viewModelScope.launch {
            val bots = listOf("SupportBot", "SalesBot", "FAQBot")
            val latest = bots.map { bot ->
                val last = repository.getLatestMessage(bot)
                ConversationModel(bot, last?.msg ?: "Say hi to $bot")
            }
            _conversations.value = latest
        }
    }
}

