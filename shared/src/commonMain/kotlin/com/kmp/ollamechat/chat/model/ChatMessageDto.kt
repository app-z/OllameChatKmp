package com.kmp.ollamechat.chat.model

import com.kmp.ollamechat.core.network.client.model.ChatRole
import com.kmp.ollamechat.core.network.repository.model.ChatMessageDto

//import com.kmp.ollamechat.client.model.ChatRole

data class ChatMessageDto_(
    val id: Long = 0L,
    val role: ChatRole,          // "system" | "user" | "assistant" | "tool"
    val content: String,
    val thinking: String? = null,
    val imagePath: String? = null
)



fun ChatMessageDto.toChatMessageUi(): ChatMessageUi {
    return ChatMessageUi(
        id = id,
        role = role,
        content = content,
        thinking = thinking,
        imageFile = null
    )
}
