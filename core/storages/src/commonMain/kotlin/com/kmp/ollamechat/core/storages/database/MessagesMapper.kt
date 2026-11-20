package com.kmp.ollamechat.core.storages.database

import com.kmp.ollamechat.core.domain.model.OllamaMessage

fun MessagesEntity.toChatMessage(): OllamaMessage {
    return OllamaMessage(
        id = id,
        content = content,
        role = role,
        thinking = thinking,
        images = imagePath
    )
}

fun OllamaMessage.toMessagesEntity(): MessagesEntity {
    return MessagesEntity(
        id = id,
        content = content,
        role = role,
        thinking = thinking,
        imagePath = images
    )
}
