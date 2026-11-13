package com.kmp.ollamechat.repository

import com.kmp.ollamechat.client.model.OllamaChatRequest
import com.kmp.ollamechat.client.model.OllamaMessage
import com.kmp.ollamechat.repository.model.StreamOllamaEvent
import kotlinx.coroutines.flow.Flow

interface StreamRepository {
    fun sendPrompt(ollamaMessages: List<OllamaMessage>): Flow<StreamOllamaEvent>
}
