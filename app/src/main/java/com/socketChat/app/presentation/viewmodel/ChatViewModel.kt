package com.socketChat.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socketChat.app.domain.model.ChatModel
import com.socketChat.app.domain.repository.ChatRepository
import com.socketChat.app.utils.Status
import com.socketChat.app.utils.USER_MESSAGE_ID
import com.socketChat.app.utils.WebSocketManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ChatViewModel @Inject constructor(private val repository: ChatRepository) : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatModel>>(emptyList())
    val messages: StateFlow<List<ChatModel>> = _messages
    private val _status = MutableStateFlow(Status.DISCONNECTED)
    val status: StateFlow<Status> = _status


    fun loadHistory(botName: String) {
        viewModelScope.launch {
            val history = repository.getMessageHistory(botName)
            _messages.value = history
        }
    }

    fun setStatus(status: Status) {
        _status.value = status
    }

    fun connectSocket(botName: String) {
        WebSocketManager.connect(botName = botName,
            onMessageReceived = { message ->
                _messages.value += message
                viewModelScope.launch {
                    repository.saveMessageToHistory(message.msg, botName, true)
                }
            },
            onConnected = { setStatus(Status.CONNECTED) },
            onDisconnected = { setStatus(Status.DISCONNECTED) })
    }

    fun disconnectSocket() {
        WebSocketManager.disconnect()
    }


    fun trySendMessage(message: String, botName: String) {
        val msg = ChatModel(message, fromMe = true, botName = botName)
        _messages.value += msg
        viewModelScope.launch {
            repository.saveMessageToHistory(message, botName, true)
        }
        WebSocketManager.send("$USER_MESSAGE_ID $message")
    }

}


