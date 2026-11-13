package com.kmp.ollamechat.database

import com.kmp.ollamechat.chat.model.ChatMessage

/**
 * Маппинг между сущностью БД и доменной моделью.
 */

fun MessagesEntity.toChatMessage(): ChatMessage {
    return ChatMessage(
        id = id,
        content = content,
        role = role,
        thinking = thinking
    )
}

fun ChatMessage.toMessagesEntity(): MessagesEntity {
    return MessagesEntity(
        id = id,
        content = content,
        role = role,
        thinking = thinking,
        dateUtc = null // На текущий момент в ChatMessage нет поля для даты
    )
}
