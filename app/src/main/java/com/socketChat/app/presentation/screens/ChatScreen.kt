package com.socketChat.app.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.socketChat.app.domain.model.ChatModel
import com.socketChat.app.presentation.viewmodel.ChatViewModel
import com.socketChat.app.utils.Status

@Composable
fun ChatScreen(botName: String, onBack: () -> Unit, viewModel: ChatViewModel = hiltViewModel()) {

    LaunchedEffect(botName) {
        viewModel.loadHistory(botName)
        viewModel.connectSocket(botName)
    }

    val messages = viewModel.messages.collectAsState().value
    val status = viewModel.status.collectAsState().value

    ChatUI(botName, messages,status = status, onSend = {
        viewModel.trySendMessage(it, botName)
    }, onBack = {
        viewModel.disconnectSocket()
        onBack()
    })


}

@Preview(showBackground = true)
@Composable
fun PreviewChatScreen() {
    val sampleMessages = listOf(ChatModel("Hey, how can I help you?", fromMe = false),
        ChatModel("I need support for my order", fromMe = true))

    ChatUI(botName = "SupportBot",
        messages = sampleMessages,
        onSend = {},
        onBack = {},
        status = Status.CONNECTED)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatUI(botName: String,
    messages: List<ChatModel>,
    onSend: (String) -> Unit,
    onBack: () -> Unit,
    status: Status) {
    var input by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    val lastIndex = messages.lastIndex
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(status) {
        if (status == Status.CONNECTED) snackbarHostState.showSnackbar("🟢 Connected to $botName")
    }

    LaunchedEffect(lastIndex) {
        if (lastIndex >= 0) {
            listState.animateScrollToItem(lastIndex)
        }
    }


    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
        TopAppBar(title = { Text(botName) }, navigationIcon = {
            IconButton(onClick = { onBack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
        })
    }, bottomBar = {
        Row(modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(modifier = Modifier.weight(1f),
                value = input,
                onValueChange = { input = it },
                placeholder = { Text("Type a message...") },
                maxLines = 3)
            IconButton(onClick = {
                if (input.isNotBlank()) {
                    onSend(input.trim())
                    input = ""
                }
            }) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send")
            }
        }
    }) { padding ->
        LazyColumn(
            state = listState,
            modifier = Modifier
            .fillMaxSize()
            .padding(padding),
            verticalArrangement = Arrangement.spacedBy(6.dp)) {
            items(messages) { message ->
                ChatBubble(message)
            }
        }
    }
}




