package com.kmp.ollamechat.core.storages.repository

import com.kmp.ollamechat.core.network.repository.model.ChatMessageDto
import com.kmp.ollamechat.core.storages.database.MessagesDao
import com.kmp.ollamechat.core.storages.database.toChatMessage
import com.kmp.ollamechat.core.storages.database.toMessagesEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class LocalRepositoryImpl(
    val messagesDao: MessagesDao
) : LocalRepository {
    override suspend fun getMessages(): List<ChatMessageDto> {
        return messagesDao.getAllMessages().first().map { it.toChatMessage() }
    }

    override fun observeMessages(): Flow<List<ChatMessageDto>> =
        messagesDao.getAllMessages().map { entitiesList ->
            entitiesList.map { it.toChatMessage() }
        }

    override suspend fun insertMessage(chatMessageDto: ChatMessageDto): Long =
        messagesDao.insertMessage(chatMessageDto.toMessagesEntity())

    override suspend fun updateMessage(chatMessageDto: ChatMessageDto) =
        messagesDao.updateMessage(chatMessageDto.toMessagesEntity())
}
