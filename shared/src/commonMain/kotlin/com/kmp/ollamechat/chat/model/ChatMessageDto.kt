package com.kmp.ollamechat.chat.model

import com.kmp.ollamechat.network.datasource.model.ChatMessageDto

fun ChatMessageDto.toChatMessageUi(): ChatMessageUi {
    return ChatMessageUi(
        id = id,
        role = role,
        content = content,
        thinking = thinking,
        imageFile = null
    )
}
