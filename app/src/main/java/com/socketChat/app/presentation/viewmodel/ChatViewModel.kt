package com.socketChat.app.presentation.viewmodel
import androidx.appcompat.widget.ThemedSpinnerAdapter.Helper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.socketChat.app.utils.Status
import com.socketChat.app.domain.model.ChatModel
import kotlinx.coroutines.launch
import com.socketChat.app.domain.repository.ChatRepository
import com.socketChat.app.utils.USER_MESSAGE_ID


class ChatViewModel(
    private val repository: ChatRepository
) : ViewModel() {

    private val _messages = MutableLiveData<MutableList<ChatModel>>(mutableListOf())
    val messages: LiveData<MutableList<ChatModel>> get() = _messages

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status> = _status

    fun loadChatHistory(bot: String) {
        viewModelScope.launch {
            val history = repository.getMessageHistory(bot)
            _messages.postValue(history.toMutableList())
        }
    }

    fun addMessageToUI(message: ChatModel) {
        val updated = _messages.value ?: mutableListOf()
        updated.add(message)
        _messages.postValue(updated)
    }

    fun sendMessage(message: String, bot: String, isOnline: Boolean) {
        viewModelScope.launch {
            if (isOnline) {
                val sent = repository.sendMessage(USER_MESSAGE_ID + message)
                if (!sent) {
                    repository.queueMessage(message, bot)
                }
            } else {
                repository.queueMessage(message, bot)
            }

            repository.saveMessageToHistory(message, bot, fromMe = true)
            addMessageToUI(ChatModel(message, fromMe = true))
        }
    }

    fun onMessageReceived(message: String, bot: String) {
        viewModelScope.launch {
            repository.saveMessageToHistory(message, bot, fromMe = false)
            addMessageToUI(ChatModel(message, fromMe = false))
        }
    }

    fun retryQueuedMessages(bot: String) {
        viewModelScope.launch {
            val messages = repository.getQueuedMessages()
                .filter { it.botName == bot }

            messages.forEach {
                val sent = repository.sendMessage(it.message)
                if (sent) {
                    repository.removeQueuedMessage(it)
                }
            }
        }
    }

    fun setStatus(status: Status) {
        _status.postValue(status)
    }
}

