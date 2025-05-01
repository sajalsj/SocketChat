package com.socketChat.app.utils

import com.socketChat.app.presentation.viewmodel.ChatViewModel
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class WebSocketListener(
    private val viewModel: ChatViewModel,
    private val botName: String

) : WebSocketListener() {
    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        viewModel.setStatus(Status.DISCONNECTED)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        viewModel.setStatus(Status.DISCONNECTED)
    }

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        if (viewModel.status.value == Status.CONNECTED) {
            if (!text.contains(USER_MESSAGE_ID)){
                val botName = botName // add this field or pass via constructor
                viewModel.onMessageReceived(text, botName)
            }

        }
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        viewModel.setStatus(Status.CONNECTED)
    }
}