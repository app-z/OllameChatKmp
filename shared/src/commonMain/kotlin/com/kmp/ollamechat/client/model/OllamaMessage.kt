package com.kmp.ollamechat.client.model

import kotlinx.serialization.Serializable

@Serializable
data class OllamaMessage(
    val role: ChatRole,
    val content: String,
    val thinking: String? = null, // Для моделей с режимом рассуждений (например, DeepSeek-R1 или Gemma 4)
    val images: List<String>? = null
)
