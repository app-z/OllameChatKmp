package com.kmp.ollamechat.chat

//import com.kmp.ollamechat.client.model.ChatRole
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.kmp.ollamechat.chat.model.ChatMessageUi
import com.kmp.ollamechat.core.domain.model.ChatRole
import com.kmp.ollamechat.util.toBoldAnnotatedString

@Composable
fun MessageBubble(
    message: ChatMessageUi,
    onSpeak: (String) -> Unit,
    isStreaming: Boolean
) {

    val isUser = message.role == ChatRole.USER
    val bubbleColor = if (isUser) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }
    val alignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart

    // По умолчанию раскрыто, пока идет процесс размышления и нет основного ответа
    var isThinkingExpanded by remember(message.id) {
        mutableStateOf(message.content.isEmpty())
    }

    val hasContent = message.content.isNotEmpty()

    // Автоматически сворачиваем блок, как только приходит первый токен основного ответа
    LaunchedEffect(hasContent) {
        if (hasContent) {
            isThinkingExpanded = false
        }
    }

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
                    ThinkingSection(
                        thinking = message.thinking,
                        isExpanded = isThinkingExpanded,
                        isThinkingInProgress = !hasContent,
                        onToggle = { isThinkingExpanded = !isThinkingExpanded }
                    )
                    Spacer(Modifier.height(6.dp))
                }

                if (message.content.isNotEmpty()) {
                    Text(
                        text = message.content.toBoldAnnotatedString(),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                if (message.content.isEmpty() && message.thinking.isNullOrEmpty()) {
                    TypingIndicator()
                }

                message.imageFile?.let { file ->
                    Spacer(Modifier.height(6.dp))
                    AsyncImage(
                        model = file,
                        contentDescription = "Прикрепленное изображение",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 240.dp)
                            .clip(RoundedCornerShape(12.dp))
                    )
                }

                if (!isUser && message.content.isNotEmpty() && !isStreaming) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = remember(message.id) { { onSpeak(message.content) } },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                contentDescription = "Озвучить ответ",
                                modifier = Modifier.size(18.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

            }
        }
    }
}

