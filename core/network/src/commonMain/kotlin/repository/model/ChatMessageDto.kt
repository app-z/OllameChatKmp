package com.kmp.ollamechat.core.network.repository.model

import com.kmp.ollamechat.core.network.client.model.ChatRole

data class ChatMessageDto(
    val id: Long = 0L,
    val role: ChatRole,          // "system" | "user" | "assistant" | "tool"
    val content: String,
    val thinking: String? = null,
    val imagePath: String? = null
)
