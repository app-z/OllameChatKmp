package com.kmp.ollamechat.client.model

import kotlinx.serialization.Serializable

@Serializable
data class OllamaMessage(
    val role: ChatRole,
    val content: String,
    val thinking: String? = null,
    val images: List<String>? = null
)
