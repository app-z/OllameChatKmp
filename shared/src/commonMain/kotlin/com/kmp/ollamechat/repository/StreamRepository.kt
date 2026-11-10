package com.kmp.ollamechat.repository

import com.kmp.ollamechat.client.model.ChatRequest
import com.kmp.ollamechat.repository.model.StreamOllamaEvent
import kotlinx.coroutines.flow.Flow

interface StreamRepository {
    fun sendPrompt(chatRequests: List<ChatRequest>): Flow<StreamOllamaEvent>
}
