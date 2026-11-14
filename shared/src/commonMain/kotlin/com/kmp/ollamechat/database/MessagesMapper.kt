package com.kmp.ollamechat.database

import com.kmp.ollamechat.chat.model.ChatMessageDto

/**
 * Маппинг между сущностью БД и доменной моделью.
 */

fun MessagesEntity.toChatMessage(): ChatMessageDto {
    return ChatMessageDto(
        id = id,
        content = content,
        role = role,
        thinking = thinking,
        imagePath = imagePath
    )
}

fun ChatMessageDto.toMessagesEntity(): MessagesEntity {
    return MessagesEntity(
        id = id,
        content = content,
        role = role,
        thinking = thinking,
        imagePath = imagePath
    )
}
