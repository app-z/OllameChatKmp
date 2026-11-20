package com.kmp.ollamechat.core.datasource

import com.kmp.ollamechat.core.domain.model.ChatRole
import com.kmp.ollamechat.core.domain.model.OllamaMessage
import kotlinx.coroutines.flow.Flow

interface LocalChatDataSource {
    suspend fun updateMessage(chatMessage: OllamaMessage)

    suspend fun getMessages(): List<OllamaMessage>

    fun observeMessages(): Flow<List<OllamaMessage>>

    suspend fun insertMessage(
        role: ChatRole,
        thinking: String?,
        content: String,
        imagePath: List<String>
    ): Long

}