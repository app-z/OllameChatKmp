package com.kmp.ollamechat.client.model

import com.kmp.ollamechat.core.network.client.model.ChatRole
import kotlinx.serialization.Serializable

@Serializable
data class OllamaMessage(
    val role: ChatRole,
    val content: String,
    val thinking: String? = null,
    val images: List<String>? = null
)
