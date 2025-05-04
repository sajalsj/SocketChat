package com.socketChat.app.presentation.screens
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.socketChat.app.domain.model.ChatModel

@Composable
fun ChatBubble(message: ChatModel) {
    val bubbleColor = if (message.fromMe) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
    val textColor = if (message.fromMe) Color.White else Color.Black
    val alignment = if (message.fromMe) Alignment.End else Alignment.Start

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        horizontalArrangement = if (message.fromMe) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = bubbleColor,
            tonalElevation = 4.dp
        ) {
            Text(
                text = message.msg,
                color = textColor,
                modifier = Modifier.padding(12.dp),
                maxLines = 10
            )
        }
    }
}
