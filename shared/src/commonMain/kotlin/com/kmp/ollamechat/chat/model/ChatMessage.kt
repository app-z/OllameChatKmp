package com.kmp.ollamechat.chat.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
data class ChatMessage(
    val id: Long,
    val role: ChatRole,          // "system" | "user" | "assistant" | "tool"
    val content: String,
    val thinking: String? = null,
)


@Serializable
enum class ChatRole {
    @SerialName("system")
    SYSTEM,

    @SerialName("user")
    USER,

    @SerialName("assistant")
    ASSISTANT,

    @SerialName("tool")
    TOOL
}