package com.socketChat.app.domain.model

data class ChatModel(
    val msg: String,
    val fromMe: Boolean,
    val botName: String = ""
)
