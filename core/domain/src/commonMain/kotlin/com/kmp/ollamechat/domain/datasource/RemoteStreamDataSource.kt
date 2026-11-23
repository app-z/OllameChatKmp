package com.kmp.ollamechat.domain.datasource

import com.kmp.ollamechat.domain.model.OllamaMessage
import com.kmp.ollamechat.domain.model.OllamaModel
import com.kmp.ollamechat.domain.model.StreamOllamaEvent
import kotlinx.coroutines.flow.Flow

interface RemoteStreamDataSource {
    fun sendPrompt(ollamaMessages: List<OllamaMessage>): Flow<StreamOllamaEvent>

    suspend fun getModels(): Result<List<OllamaModel>>
}
