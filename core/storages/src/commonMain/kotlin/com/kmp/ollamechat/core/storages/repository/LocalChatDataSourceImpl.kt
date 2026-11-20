package com.kmp.ollamechat.core.storages.repository

import com.kmp.ollamechat.core.datasource.LocalChatDataSource
import com.kmp.ollamechat.core.domain.model.ChatRole
import com.kmp.ollamechat.core.domain.model.OllamaMessage
import com.kmp.ollamechat.core.storages.database.MessagesDao
import com.kmp.ollamechat.core.storages.database.toChatMessage
import com.kmp.ollamechat.core.storages.database.toMessagesEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class LocalChatDataSourceImpl(
    val messagesDao: MessagesDao
) : LocalChatDataSource {
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
