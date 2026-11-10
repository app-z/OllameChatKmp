package com.kmp.ollamechat.useCase

import com.kmp.ollamechat.chat.model.ChatRole
import com.kmp.ollamechat.client.model.ChatRequest
import com.kmp.ollamechat.repository.StreamRepository
import com.kmp.ollamechat.repository.model.StreamOllamaEvent
import kotlinx.coroutines.flow.Flow

class StreamUseCase(val streamRepository: StreamRepository) {

    fun sendPrompt(role: ChatRole, content: String): Flow<StreamOllamaEvent> {
        return streamRepository.sendPrompt(
            listOf(ChatRequest(role = role,
                content = content))
        )
    }
}
