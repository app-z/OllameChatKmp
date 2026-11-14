package com.kmp.ollamechat.chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.kmp.ollamechat.chat.model.ChatMessageUi
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.dialogs.FileKitType
import io.github.vinceglb.filekit.dialogs.compose.rememberFilePickerLauncher
import kotlinx.coroutines.launch

@Composable
fun ChatScreenContent(
    messages: List<ChatMessageUi>,
    isStreaming: Boolean,
    onSend: (String, PlatformFile?) -> Unit,
    onStop: () -> Unit,
    onSpeak: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    var pickedFile by remember { mutableStateOf<PlatformFile?>(null) }

    // Флаг разрешения автоскролла
    var isAutoScrollEnabled by remember { mutableStateOf(true) }

    val canScrollForward by remember {
        derivedStateOf { listState.canScrollForward }
    }

    // Возобновляем автоскролл, когда пользователь сам докрутил вниз и отпустил палец
    LaunchedEffect(canScrollForward, listState.isScrollInProgress) {
        if (!canScrollForward && !listState.isScrollInProgress) {
            isAutoScrollEnabled = true
        }
    }

    val launcher = rememberFilePickerLauncher(
        type = FileKitType.Image
    ) { file ->
        pickedFile = file
    }

    val lastContentLen = messages.lastOrNull()?.content?.length ?: 0
    val lastThinkingLen = messages.lastOrNull()?.thinking?.length ?: 0

    // Скроллим вниз при новых токенах, ТОЛЬКО если автоскролл включен
    LaunchedEffect(messages.size, lastContentLen, lastThinkingLen, isStreaming) {
        if (messages.isEmpty() || !isAutoScrollEnabled) return@LaunchedEffect

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
                onStop = onStop,
                onAttach = {
                    launcher.launch()
                },
                onRemoveAttachment = {
                    pickedFile = null
                }
            )
        }
    ) { paddingValues ->
        // Оборачиваем список в Box, чтобы разместить кнопку поверх в правом нижнем углу
        Box(
            modifier = modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        awaitEachGesture {
                            awaitFirstDown(requireUnconsumed = false)
                            isAutoScrollEnabled = false
                        }
                    },
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(messages, key = { message -> message.id }) { msg ->
                    MessageBubble(msg, onSpeak = onSpeak)
                }
            }

            // Плавающая кнопка возврата в самый низ чата
            AnimatedVisibility(
                visible = !isAutoScrollEnabled && messages.isNotEmpty(),
                enter = fadeIn() + scaleIn(),
                exit = fadeOut() + scaleOut(),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                SmallFloatingActionButton(
                    onClick = {
                        isAutoScrollEnabled = true
                        scope.launch {
                            listState.animateScrollToItem(
                                index = messages.lastIndex,
                                scrollOffset = 10000
                            )
                        }
                    },
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                ) {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "В конец чата"
                    )
                }
            }
        }
    }
}
