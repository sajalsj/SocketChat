package com.socketChat.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.socketChat.app.domain.repository.ChatRepository

class ConversationViewModelFactory(
    private val repository: ChatRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ConversationViewModel(repository) as T
    }
}
