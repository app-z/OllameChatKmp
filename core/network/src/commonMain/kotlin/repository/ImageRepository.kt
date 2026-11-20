package com.kmp.ollamechat.core.network.repository

import  com.kmp.ollamechat.core.network.client.model.OllamaChatRequest
import  com.kmp.ollamechat.core.network.client.model.OllamaChatResponse

interface ImageRepository {
    suspend fun sendImagePrompt(
        chatRequests: OllamaChatRequest
    ): OllamaChatResponse
}
