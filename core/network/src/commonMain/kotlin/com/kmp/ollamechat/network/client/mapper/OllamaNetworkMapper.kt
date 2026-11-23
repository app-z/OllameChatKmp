package com.kmp.ollamechat.network.client.mapper

import com.kmp.ollamechat.domain.model.OllamaMessage
import com.kmp.ollamechat.domain.model.OllamaModel
import com.kmp.ollamechat.network.client.model.OllamaModelDto
import com.kmp.ollamechat.network.datasource.model.ChatMessageDto


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


fun OllamaModelDto.toOllamaModel(): OllamaModel {
    return OllamaModel(
        name = name,
        model = model,
        size = size,
        digest = digest,
        modifiedAt = modifiedAt
    )
}

fun OllamaModel.toOllamaModelDto(): OllamaModelDto {
    return OllamaModelDto(
        name = name,
        model = model,
        size = size,
        digest = digest,
        modifiedAt = modifiedAt
    )
}
