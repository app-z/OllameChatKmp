package com.kmp.ollamechat.chat

import kotlinx.serialization.Serializable

@Serializable
data class ChatRequest(
    val role: String,          // "system" | "user" | "assistant" | "tool"
    val content: String,
    val thinking: String? = null,
    val images: List<String>? = null
)
