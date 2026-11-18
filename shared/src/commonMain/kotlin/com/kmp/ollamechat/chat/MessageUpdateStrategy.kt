package com.kmp.ollamechat.chat

import com.kmp.ollamechat.chat.model.ChatMessageDto
import com.kmp.ollamechat.client.model.ChatRole
import com.kmp.ollamechat.repository.Settings
import com.kmp.ollamechat.useCase.LocalUseCase
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableSharedFlow
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

    suspend fun updateMessage(
        messageId: Long,
        thinkingBuilder: StringBuilder,
        contentBuilder: StringBuilder,
        currentMessage: MutableSharedFlow<ChatMessageDto?>
    ) {


        when (strategy) {
            StrategyType.DIRECTLY_TO_DB -> {
                if (lastSaveMark.elapsedNow() >= settings.getIntervalForSaveMessageToDb()) {
                    lastSaveMark = timeSource.markNow()
                    saveToDb(messageId, thinkingBuilder, contentBuilder)
                }
            }

            StrategyType.AS_FLOW -> {
                currentMessage.emit(
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

    suspend fun saveLastWordToDb(
        messageId: Long,
        thinkingBuilder: StringBuilder,
        contentBuilder: StringBuilder
    ) {
        // Save last word
        withContext(NonCancellable) {
            saveToDb(messageId, thinkingBuilder, contentBuilder)
        }

    }

    private suspend fun saveToDb(
        id: Long,
        thinkingBuilder: StringBuilder,
        contentBuilder: StringBuilder
    ) {
        localUseCase.updateMessage(
            ChatMessageDto(
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