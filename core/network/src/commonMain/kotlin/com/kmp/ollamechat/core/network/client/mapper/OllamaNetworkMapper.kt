package com.kmp.ollamechat.core.network.client.mapper

import com.kmp.ollamechat.core.domain.model.OllamaMessage
import com.kmp.ollamechat.core.network.repository.model.ChatMessageDto


internal fun OllamaMessage.toChatMessageDto(base64Image: String? = null): ChatMessageDto {
    return ChatMessageDto(
        id = id,
        role = role, // "user", "assistant", "system"
        content = content,
        thinking = thinking,
        imagePath = base64Image
    )
}

internal fun List<OllamaMessage>.toChatMessageDtoList(): List<ChatMessageDto> {
    return map { it.toChatMessageDto() }
}