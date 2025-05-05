package com.socketChat.app.presentation.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.socketChat.app.domain.model.ConversationModel
import com.socketChat.app.presentation.viewmodel.ConversationViewModel
import androidx.compose.runtime.collectAsState


@Composable
fun ConversationScreen(viewModel: ConversationViewModel = hiltViewModel(),
    onBotClick: (String) -> Unit) {
    val conversations = viewModel.conversations.collectAsState(initial = emptyList()).value

    LaunchedEffect(Unit) {
        viewModel.getLatestMessage()
    }
    ConversationListUI(conversations = conversations, onBotClick = onBotClick)
}

@Composable
fun PreviewConversationScreen() {
    val dummyList = listOf(
        ConversationModel("SupportBot", "How can I help you?", 2),
        ConversationModel("SalesBot", "Limited time offer!", 0),
        ConversationModel("FAQBot", "Ask me anything!", 1),
    )

    ConversationListUI(conversations = dummyList) {
        println("Clicked bot: $it")
    }
}
@OptIn(ExperimentalMaterial3Api::class)

@Composable
fun ConversationListUI(conversations: List<ConversationModel>,onBotClick: (String) -> Unit) {
    Scaffold(topBar = {
        TopAppBar(title = { Text("Conversations") })
    }) { padding ->
        LazyColumn(contentPadding = padding,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)) {
            items(conversations) { conversation ->
                BotCard(conversation, onClick = { onBotClick(conversation.botName) })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewConversationScreenLight() {
    PreviewConversationScreen()
}

