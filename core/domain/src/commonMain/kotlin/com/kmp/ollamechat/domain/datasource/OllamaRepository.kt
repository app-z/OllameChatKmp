package com.kmp.ollamechat.domain.datasource

import com.kmp.ollamechat.domain.model.OllamaMessage
import com.kmp.ollamechat.domain.model.OllamaModel
import com.kmp.ollamechat.domain.model.StreamOllamaEvent
import kotlinx.coroutines.flow.Flow

interface OllamaRepository {
    fun sendPrompt(ollamaMessages: List<OllamaMessage>, modelName: String): Flow<StreamOllamaEvent>

    suspend fun getModels(): Result<List<OllamaModel>>
}
