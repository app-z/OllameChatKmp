package com.kmp.ollamechat.chat

import androidx.compose.foundation.interaction.DragInteraction
import androidx.compose.foundation.interaction.PressInteraction
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kmp.ollamechat.chat.model.ChatMessage
import com.kmp.ollamechat.client.model.ChatRole
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher

@Composable
fun ChatScreenContent(
    messages: List<ChatMessage>,
    isStreaming: Boolean,
    onSend: (String, PlatformFile?) -> Unit,
    onStop: () -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    var pickedFile by remember { mutableStateOf<PlatformFile?>(null) }

    //-------------------------------------------------------------------------
    // Флаг разрешения автоскролла
    var isAutoScrollEnabled by remember { mutableStateOf(true) }


    // 1. Останавливаем автоскролл, как только пользователь нажал на список или начал свайп
    LaunchedEffect(listState.interactionSource) {
        listState.interactionSource.interactions.collect { interaction ->
            if (interaction is PressInteraction.Press || interaction is DragInteraction.Start) {
                isAutoScrollEnabled = false
            }
        }
    }

    // 2. Если пользователь сам докрутил список до самого низа — снова включаем автоскролл
    val isAtBottom by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val lastVisibleItem = layoutInfo.visibleItemsInfo.lastOrNull()
            lastVisibleItem != null &&
                    lastVisibleItem.index == layoutInfo.totalItemsCount - 1 &&
                    lastVisibleItem.offset + lastVisibleItem.size <= layoutInfo.viewportEndOffset + 20
        }
    }

    LaunchedEffect(isAtBottom) {
        if (isAtBottom && !listState.isScrollInProgress) {
            isAutoScrollEnabled = true
        }
    }

    //------------------------------------------------------------------------

    val launcher = rememberFilePickerLauncher(
        type = FileKitType.Image
    ) { file ->
        pickedFile = file
    }

    val lastContentLen = messages.lastOrNull()?.content?.length ?: 0
    val lastThinkingLen = messages.lastOrNull()?.thinking?.length ?: 0

    // 3. Скроллим ТОЛЬКО если isAutoScrollEnabled == true
    LaunchedEffect(messages.size, lastContentLen, lastThinkingLen, isStreaming) {
        if (messages.isEmpty() || !isAutoScrollEnabled) return@LaunchedEffect

        // Используем scrollToItem, чтобы плавная анимация не перехватывала управление у пальца
        listState.scrollToItem(
            index = messages.lastIndex,
            scrollOffset = 10000
        )
    }


    Scaffold(
        bottomBar = {
            ChatInputBar(
                attachedFile = pickedFile,
                isStreaming = isStreaming,
                onSend = { text ->
                    isAutoScrollEnabled = true
                    onSend(text, pickedFile)
                    pickedFile = null
                },
                onStop = onStop, {
                    launcher.launch()
                },
                onRemoveAttachment = {
                    pickedFile = null
                }
            )
        }
    ) { paddingValues ->
        // 2 ЧАСТЬ: Центральная область со списком сообщений
        LazyColumn(
            state = listState,
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages, key = { message -> message.id }) { msg ->
                MessageBubble(msg)
            }
        }
    }
}

@Composable
@Preview
fun ChatScreenContentPreview() {

    val messages = mutableListOf<ChatMessage>()

    for (i in 1 until 30 step 2) {
        messages.add(
            ChatMessage(
                id = i.toLong(),
                role = ChatRole.USER,          // "system" | "user" | "assistant" | "tool"
                content = "Gjgjhgjhg $i",
                thinking = null,
            )
        )

        messages.add(
            ChatMessage(
                id = i + 1L, // !!
                role = ChatRole.ASSISTANT,          // "system" | "user" | "assistant" | "tool"
                content = "Answer $i",
                thinking = "Think",
            )
        )

    }

    ChatScreenContent(
        messages = messages,
        isStreaming = true,
        onSend = { string, file ->

        },
        onStop = {}
    )
}