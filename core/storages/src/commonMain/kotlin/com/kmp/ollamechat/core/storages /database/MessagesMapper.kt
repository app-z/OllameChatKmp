package com.kmp.ollamechat.core.storages.database

//import com.kmp.ollamechat.chat.model.ChatMessageDto
//import com.kmp.ollamechat.chat.model.ChatMessageUi
import com.kmp.ollamechat.core.network.repository.model.ChatMessageDto

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

//fun ChatMessageDto.toChatMessageUi(): ChatMessageUi {
//    return ChatMessageUi(
//        id = id,
//        role = role,
//        content = content,
//        thinking = thinking,
//        imageFile = null
//    )
//}
