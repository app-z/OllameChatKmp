package com.kmp.ollamechat.network.datasource.model

import com.kmp.ollamechat.domain.model.ChatRole
import kotlinx.serialization.Serializable


@Serializable
data class ChatMessageDto(
    val id: Long = 0L,
    val role: ChatRole,          // "system" | "user" | "assistant" | "tool"
    val content: String,
    val thinking: String? = null,
    val imagePath: String? = null
)
