package com.kmp.ollamechat.repository

import com.kmp.ollamechat.client.model.OllamaChatRequest
import com.kmp.ollamechat.client.model.OllamaChatResponse

interface ImageRepository {
    suspend fun sendImagePrompt(
        chatRequests: OllamaChatRequest
    ): OllamaChatResponse
}
