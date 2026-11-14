package com.kmp.ollamechat.chat

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
        onSpeak = viewModel::speak,
        modifier = modifier
    )
}


