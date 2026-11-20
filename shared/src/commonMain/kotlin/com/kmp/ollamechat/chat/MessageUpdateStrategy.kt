package com.kmp.ollamechat.chat

import com.kmp.ollamechat.core.domain.model.ChatRole
import com.kmp.ollamechat.core.domain.model.OllamaMessage
import com.kmp.ollamechat.core.network.repository.Settings
import com.kmp.ollamechat.core.network.repository.model.ChatMessageDto
import com.kmp.ollamechat.core.usecase.LocalUseCase
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import kotlin.time.TimeSource

// Not real Strategy pattern
class MessageUpdateStrategy(
    private val localUseCase: LocalUseCase,
    private val settings: Settings
) {

    private val strategy: StrategyType = StrategyType.AS_FLOW

    private val timeSource = TimeSource.Monotonic
    private var lastSaveMark = timeSource.markNow()

    private val _currentMessage = MutableStateFlow<ChatMessageDto?>(null)
    val currentMessage = _currentMessage.asStateFlow()

    val thinkingBuilder = StringBuilder()
    val contentBuilder = StringBuilder()

    suspend fun updateContent(
        messageId: Long,
        content: String
    ) {
        contentBuilder.append(content)
        updateMessage(messageId)
    }

    suspend fun updateThinking(
        messageId: Long,
        thinking: String
    ) {
        thinkingBuilder.append(thinking)
        updateMessage(messageId)
    }

    private suspend fun updateMessage(
        messageId: Long
    ) {
        when (strategy) {
            StrategyType.DIRECTLY_TO_DB -> {
                if (lastSaveMark.elapsedNow() >= settings.getIntervalForSaveMessageToDb()) {
                    lastSaveMark = timeSource.markNow()
                    saveToDb(messageId, thinkingBuilder, contentBuilder)
                }
            }

            StrategyType.AS_FLOW -> {
                _currentMessage.emit(
                    ChatMessageDto(
                        id = messageId,
                        role = ChatRole.ASSISTANT,
                        thinking = thinkingBuilder.toString().ifEmpty { null },
                        content = contentBuilder.toString()
                    )
                )

            }
        }
    }

    suspend fun saveLastWordToDbAndClean(
        messageId: Long
    ) {
        // Save last word
        withContext(NonCancellable) {
            saveToDb(messageId, thinkingBuilder, contentBuilder)
        }

        clean()
    }

    fun clean() {
        thinkingBuilder.clear()
        contentBuilder.clear()
    }

    private suspend fun saveToDb(
        id: Long,
        thinkingBuilder: StringBuilder,
        contentBuilder: StringBuilder
    ) {
        localUseCase.updateMessage(
            OllamaMessage(
                id = id,
                role = ChatRole.ASSISTANT,
                thinking = thinkingBuilder.toString().ifEmpty { null },
                content = contentBuilder.toString()
            )
        )
    }

    enum class StrategyType {
        DIRECTLY_TO_DB,
        AS_FLOW
    }

}