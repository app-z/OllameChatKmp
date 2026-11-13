package com.kmp.ollamechat.chat.model

import com.kmp.ollamechat.client.model.ChatRole

data class ChatMessage(
    val id: Long = 0L,
    val role: ChatRole,          // "system" | "user" | "assistant" | "tool"
    val content: String,
    val thinking: String? = null,
)
