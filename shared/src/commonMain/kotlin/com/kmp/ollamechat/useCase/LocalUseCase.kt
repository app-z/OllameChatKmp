package com.kmp.ollamechat.useCase

import com.kmp.ollamechat.chat.model.ChatMessage
import com.kmp.ollamechat.repository.LocalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class LocalUseCase(val localRepository: LocalRepository) {
    fun observeMessages(): Flow<List<ChatMessage>> =
        localRepository.observeMessages()

    suspend fun getMessages(): List<ChatMessage> = withContext(Dispatchers.IO) {
        localRepository.getMessages()
    }

    suspend fun insertMessage(message: ChatMessage) = withContext(Dispatchers.IO) {
        localRepository.insertMessage(message)
    }

    suspend fun updateMessage(message: ChatMessage) = withContext(Dispatchers.IO) {
        localRepository.updateMessage(message)
    }

}