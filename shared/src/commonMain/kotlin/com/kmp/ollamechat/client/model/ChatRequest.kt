package com.kmp.ollamechat.client.model

import com.kmp.ollamechat.chat.model.ChatRole
import kotlinx.serialization.Serializable

@Serializable
data class ChatRequest(
    val role: ChatRole,          // "system" | "user" | "assistant" | "tool"
    val content: String,
    val thinking: String? = null,
    val images: List<String>? = null
)
