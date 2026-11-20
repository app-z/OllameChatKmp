package com.kmp.ollamechat.core.datasource

import com.kmp.ollamechat.core.domain.model.OllamaMessage
import com.kmp.ollamechat.core.domain.model.StreamOllamaEvent
import kotlinx.coroutines.flow.Flow

interface RemoteStreamDataSource {
    fun sendPrompt(ollamaMessages: List<OllamaMessage>): Flow<StreamOllamaEvent>
}
