package com.kmp.ollamechat.storages.datasource

import com.kmp.ollamechat.common.AppDispatchers
import com.kmp.ollamechat.domain.datasource.LocalChatDataSource
import com.kmp.ollamechat.domain.model.ChatRole
import com.kmp.ollamechat.domain.model.OllamaMessage
import com.kmp.ollamechat.storages.database.MessagesDao
import com.kmp.ollamechat.storages.database.toChatMessage
import com.kmp.ollamechat.storages.database.toMessagesEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

class LocalChatDataSourceImpl(
    private val messagesDao: MessagesDao,
    private val dispatchers: AppDispatchers
) : LocalChatDataSource {
    override suspend fun getMessages(): List<OllamaMessage> {
        return messagesDao.getAllMessages().first().map { it.toChatMessage() }
    }

    override fun observeMessages(): Flow<List<OllamaMessage>> =
        messagesDao.getAllMessages().map { entitiesList ->
            entitiesList.map { it.toChatMessage() }
        }.flowOn(dispatchers.io)

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
