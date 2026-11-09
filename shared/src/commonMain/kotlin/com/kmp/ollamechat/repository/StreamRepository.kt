package com.kmp.ollamechat.repository

import com.kmp.ollamechat.chat.ChatRequest
import com.kmp.ollamechat.repository.model.StreamOllamaEvent
import kotlinx.coroutines.flow.Flow

interface StreamRepository {
    fun sendPrompt(messages: List<ChatRequest>): Flow<StreamOllamaEvent>
}
