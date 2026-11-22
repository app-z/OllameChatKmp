package com.kmp.ollamechat.domain.datasource

import com.kmp.ollamechat.domain.model.ChatRole
import com.kmp.ollamechat.domain.model.OllamaMessage
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