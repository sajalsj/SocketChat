package com.socketChat.app.domain.model
data class ConversationModel(
    val botName: String,
    var lastMessage: String,
    var unreadCount: Int = 0
)

