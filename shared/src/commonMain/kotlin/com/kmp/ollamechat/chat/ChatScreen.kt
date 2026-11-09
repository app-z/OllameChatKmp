package com.kmp.ollamechat.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ChatScreen(
    viewModel: ChatViewModel,
    modifier: Modifier = Modifier
) {
    val messages by viewModel.messages.collectAsStateWithLifecycle()
    val isStreaming by viewModel.isStreaming.collectAsStateWithLifecycle()

    ChatScreenContent(
        messages = messages,
        isStreaming = isStreaming,
        onSend = viewModel::send,
        onStop = viewModel::stop,
        modifier = modifier
    )
}

@Composable
private fun ChatScreenContent(
    messages: List<ChatMessage>,
    isStreaming: Boolean,
    onSend: (String) -> Unit,
    onStop: () -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    val lastContentLen = messages.lastOrNull()?.content?.length ?: 0
    val lastThinkingLen = messages.lastOrNull()?.thinking?.length ?: 0
    // Автоскролл к последнему сообщению при добавлении/стриминге
    LaunchedEffect(messages.size, lastContentLen, lastThinkingLen) {
        if (messages.isEmpty()) return@LaunchedEffect
        listState.animateScrollToItem(messages.lastIndex)
    }

    Column(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f).fillMaxWidth(),
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

        ChatInputBar(
            isStreaming = isStreaming,
            onSend = onSend,
            onStop = onStop
        )
    }
}

@Composable
private fun MessageBubble(message: ChatMessage) {
    val isUser = message.role == "user"
    val bubbleColor = if (isUser) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }
    val alignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = alignment
    ) {
        Surface(
            color = bubbleColor,
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isUser) 16.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 16.dp
            ),
            modifier = Modifier.widthIn(max = 320.dp)
        ) {
            Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
                if (!message.thinking.isNullOrEmpty()) {
                    Text(
                        text = message.thinking,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(4.dp))
                }
                Text(
                    text = message.content.ifEmpty { "…" },
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Composable
private fun TypingIndicator() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(16.dp),
            strokeWidth = 2.dp
        )
        Spacer(Modifier.width(8.dp))
        Text("Печатает…", style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun ChatInputBar(
    isStreaming: Boolean,
    onSend: (String) -> Unit,
    onStop: () -> Unit
) {
    var text by rememberSaveable { mutableStateOf("") }

    fun submit() {
        val trimmed = text.trim()
        if (trimmed.isEmpty() || isStreaming) return
        onSend(trimmed)
        text = ""
    }

    Surface(tonalElevation = 3.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            OutlinedTextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Сообщение…") },
                maxLines = 5,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = { submit() }),
                enabled = !isStreaming
            )
            Spacer(Modifier.width(8.dp))
            if (isStreaming) {
                FilledIconButton(onClick = onStop) {
                    Icon(Icons.Default.Stop, contentDescription = "Стоп")
                }
            } else {
                FilledIconButton(
                    onClick = { submit() },
                    enabled = text.isNotBlank()
                ) {
                    Icon(
                        Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Отправить"
                    )
                }
            }
        }
    }
}