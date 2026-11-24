package com.kmp.ollamechat.network.datasource.model

import com.kmp.ollamechat.domain.model.ChatRole
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ChatMessageDto(
    val role: ChatRole,          // "system" | "user" | "assistant" | "tool"
    val content: String,
    val thinking: String? = null,
    @SerialName("images")
    val imagePath: List<String>? = null
)
