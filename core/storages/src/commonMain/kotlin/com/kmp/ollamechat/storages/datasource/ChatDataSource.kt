package com.kmp.ollamechat.storages.datasource

import com.kmp.ollamechat.domain.datasource.ChatRepository
import com.kmp.ollamechat.domain.model.ChatRole
import com.kmp.ollamechat.domain.model.OllamaMessage
import com.kmp.ollamechat.storages.database.MessagesDao
import com.kmp.ollamechat.storages.database.toChatMessage
import com.kmp.ollamechat.storages.database.toMessagesEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class ChatDataSource(
    private val messagesDao: MessagesDao
) : ChatRepository {
    override suspend fun getMessages(): List<OllamaMessage> {
        return messagesDao.getAllMessages().first().map { it.toChatMessage() }
    }

    override fun observeMessages(): Flow<List<OllamaMessage>> =
        messagesDao.getAllMessages().map { entitiesList ->
            entitiesList.map { it.toChatMessage() }
        }

    override suspend fun insertMessage(
        role: ChatRole,
        thinking: String?,
        content: String,
        imagePath: List<String>): Long {

        val chatMessageDto = OllamaMessage(role = role, thinking = thinking, content = content, images = imagePath)

        return messagesDao.insertMessage(chatMessageDto.toMessagesEntity())
    }

    override suspend fun updateMessage(chatMessage: OllamaMessage) =
        messagesDao.updateMessage(chatMessage.toMessagesEntity())
}
