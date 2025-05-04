package com.socketChat.app.utils

import com.socketChat.app.domain.model.ChatModel
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class ChatWebSocketListener(
    private val botName: String,
    private val onMessage: (ChatModel) -> Unit,
    private val onConnected: () -> Unit,
    private val onDisconnected: () -> Unit
) : WebSocketListener() {

    override fun onOpen(webSocket: WebSocket, response: Response) {
        onConnected()
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        if (text.contains(USER_MESSAGE_ID)) return
        onMessage(ChatModel(text, fromMe = false, botName = botName))
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        onDisconnected()
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        onDisconnected()
    }
}
