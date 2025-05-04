package com.socketChat.app.utils

import com.socketChat.app.domain.model.ChatModel
import okhttp3.OkHttpClient
import okhttp3.WebSocket

object WebSocketManager {

    private var webSocket: WebSocket? = null
    private var listener: ChatWebSocketListener? = null

    fun connect(
        botName: String,
        onMessageReceived: (ChatModel) -> Unit,
        onConnected: () -> Unit = {},
        onDisconnected: () -> Unit = {}
    ) {
        listener = ChatWebSocketListener(
            botName = botName,
            onMessage = onMessageReceived,
            onConnected = onConnected,
            onDisconnected = onDisconnected
        )

        val request = SocketUtil.createSocketRequest()
        val client = OkHttpClient()
        webSocket = client.newWebSocket(request, listener!!)
    }

    fun send(message: String) {
        webSocket?.send(message)
    }

    fun disconnect() {
        webSocket?.close(1001, "Session ended")
        listener = null
        webSocket = null
    }
}
