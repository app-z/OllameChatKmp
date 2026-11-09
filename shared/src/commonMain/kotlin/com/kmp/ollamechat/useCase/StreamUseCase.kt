package com.kmp.ollamechat.useCase

import com.kmp.ollamechat.chat.ChatRequest
import com.kmp.ollamechat.repository.StreamRepository
import com.kmp.ollamechat.repository.model.StreamOllamaEvent
import kotlinx.coroutines.flow.Flow

class StreamUseCase(val streamRepository: StreamRepository) {

    fun sendPrompt(messages: List<ChatRequest>): Flow<StreamOllamaEvent> {
        return streamRepository.sendPrompt(messages)
    }
}
