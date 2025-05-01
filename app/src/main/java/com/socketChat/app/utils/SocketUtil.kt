package com.socketChat.app.utils
import okhttp3.Request

object SocketUtil {

    private const val SOCKET_URL = "wss://s14563.blr1.piesocket.com/v3/1" +
            "?api_key=1FWxjyppMjl93Ia15fPInOCZnrzJzmJTeh64e6X4&notify_self=1"

    fun createSocketRequest(): Request {
        return Request.Builder()
            .url(SOCKET_URL)
            .build()
    }
}
