package com.kmp.ollamechat.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kmp.ollamechat.chat.model.ChatMessage
import com.kmp.ollamechat.chat.model.ChatRole

@Composable
fun ChatScreenContent(
    messages: List<ChatMessage>,
    isStreaming: Boolean,
    onSend: (String) -> Unit,
    onStop: () -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    val lastContentLen = messages.lastOrNull()?.content?.length ?: 0
    val lastThinkingLen = messages.lastOrNull()?.thinking?.length ?: 0

    LaunchedEffect(messages.size, lastContentLen, lastThinkingLen, isStreaming) {
        if (messages.isEmpty()) return@LaunchedEffect

        val lastIndex = messages.lastIndex

        // Безопасно скроллим. Дополнительный сдвиг (например, 10000) гарантирует,
        // что если последнее сообщение длинное, мы докрутим до его САМОГО низа.
        listState.animateScrollToItem(
            index = lastIndex,
            scrollOffset = 10000
        )
    }

    Scaffold(
        bottomBar = {
            ChatInputBar(
                isStreaming = isStreaming,
                onSend = onSend,
                onStop = onStop
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(messages, key = { message ->
                    message.id
                }) { msg ->
                    MessageBubble(msg)
                }

                if (isStreaming && messages.lastOrNull()?.content.isNullOrEmpty()) {
                    item { TypingIndicator() }
                }
            }
        }
    }
}

@Composable
@Preview
fun ChatScreenContentPreview() {

    ChatScreenContent(
        messages = listOf(
            ChatMessage(
                id = 1,
                role = ChatRole.USER,          // "system" | "user" | "assistant" | "tool"
                content = "Gjgjhgjhg",
                thinking = null,
            ),
            ChatMessage(
                id = 2, // !!
                role = ChatRole.ASSISTANT,          // "system" | "user" | "assistant" | "tool"
                content = "Answer",
                thinking = "Think",
            ),
        ),
        isStreaming = true,
        onSend = {},
        onStop = {}
    )
}