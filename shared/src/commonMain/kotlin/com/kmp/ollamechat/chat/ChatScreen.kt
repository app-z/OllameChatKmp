package com.kmp.ollamechat.chat

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kmp.ollamechat.chat.model.ChatMessageUi
import com.kmp.ollamechat.domain.model.ChatRole
import ollamechat.shared.generated.resources.Res
import ollamechat.shared.generated.resources.chat
import org.jetbrains.compose.resources.stringResource

@Composable
fun ChatScreen(
    viewModel: ChatViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    
    // TODO: Make one state
    val isStreaming by viewModel.isStreaming.collectAsStateWithLifecycle()
    val messages by viewModel.messagesFlow.collectAsStateWithLifecycle()
    val title by viewModel.titleFlow.collectAsStateWithLifecycle()


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(modifier = Modifier.fillMaxWidth(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        text = title)
                },
                navigationIcon = {
                    IconButton(onClick = { onBack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) {
        ChatScreenContent(
            messages = messages,
            isStreaming = isStreaming,
            onSend = viewModel::send,
            onStop = viewModel::stop,
            onSpeak = viewModel::speak,
            modifier = modifier
        )
    }
}

@Composable
@Preview
fun ChatScreenContentPreview() {
    ChatScreenContent(
        messages = listOf(
            ChatMessageUi(
                id = 1,
                role = ChatRole.USER,
                content = "Как дела?",
                thinking = "",
                imageFile = null
            ),
            ChatMessageUi(
                id = 2,
                role = ChatRole.ASSISTANT,
                content = "Хорошо",
                thinking = "Что он спросил?",
                imageFile = null
            )
        ),
        isStreaming = true,
        onSend = { _, _ -> },
        onStop = {},
        onSpeak = {},
        modifier = Modifier
    )
}