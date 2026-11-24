package com.kmp.ollamechat.chat.model

import com.kmp.ollamechat.domain.model.OllamaMessage

fun OllamaMessage.toChatMessageUi(): ChatMessageUi {
    return ChatMessageUi(
        id = id,
        role = role,
        content = content,
        thinking = thinking,
        imageFile = null
    )
}
