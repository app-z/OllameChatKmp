package com.kmp.ollamechat.core.usecase

import com.kmp.ollamechat.core.datasource.LocalChatDataSource
import com.kmp.ollamechat.core.domain.model.ChatRole
import com.kmp.ollamechat.core.domain.model.OllamaMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class LocalUseCase(val localRepository: LocalChatDataSource) {
    fun observeMessages(): Flow<List<OllamaMessage>> =
        localRepository.observeMessages()

    suspend fun getMessages(): List<OllamaMessage> = withContext(Dispatchers.IO) {
        localRepository.getMessages()
    }

    suspend fun insertUserMessage(
        thinking: String? = null,
        content: String,
        imagePath: List<String>
    ) = withContext(Dispatchers.IO)
    {
        localRepository.insertMessage(
            role = ChatRole.USER,
            thinking = thinking,
            content = content,
            imagePath = imagePath
        )
    }

    suspend fun insertAssistantMessage(
        thinking: String? = null,
        content: String,
        imagePath: List<String> = emptyList()
    ) = withContext(Dispatchers.IO) {
        localRepository.insertMessage(
            role = ChatRole.ASSISTANT,
            thinking = thinking,
            content = content,
            imagePath = imagePath
        )
    }

    suspend fun insertSystemMessage(
        content: String,
    ) = withContext(Dispatchers.IO) {
        localRepository.insertMessage(
            role = ChatRole.SYSTEM,
            thinking = null,
            content = content,
            imagePath = emptyList()
        )
    }

    suspend fun updateMessage(message: OllamaMessage) = withContext(Dispatchers.IO) {
        localRepository.updateMessage(message)
    }

}