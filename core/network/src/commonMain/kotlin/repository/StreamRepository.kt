package com.kmp.ollamechat.core.network.repository

import  com.kmp.ollamechat.core.network.client.model.OllamaChatRequest
import  com.kmp.ollamechat.core.network.client.model.OllamaMessage
import com.kmp.ollamechat.core.network.repository.model.StreamOllamaEvent
import kotlinx.coroutines.flow.Flow

interface StreamRepository {
    fun sendPrompt(ollamaMessages: List<OllamaMessage>): Flow<StreamOllamaEvent>
}
