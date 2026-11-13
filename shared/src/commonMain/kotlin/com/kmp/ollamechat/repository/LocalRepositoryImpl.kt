package com.kmp.ollamechat.repository

import com.kmp.ollamechat.chat.model.ChatMessage
import com.kmp.ollamechat.database.MessagesDao
import com.kmp.ollamechat.database.toChatMessage
import com.kmp.ollamechat.database.toMessagesEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class LocalRepositoryImpl(
    val messagesDao: MessagesDao
) : LocalRepository {
    override suspend fun getMessages(): List<ChatMessage> {
        return messagesDao.getAllMessages().first().map { it.toChatMessage() }
    }

    override fun observeMessages(): Flow<List<ChatMessage>> =
        messagesDao.getAllMessages().map { entitiesList ->
            entitiesList.map { it.toChatMessage() }
        }

    override suspend fun insertMessage(chatMessage: ChatMessage): Long =
        messagesDao.insertMessage(chatMessage.toMessagesEntity())

    override suspend fun updateMessage(chatMessage: ChatMessage) =
        messagesDao.updateMessage(chatMessage.toMessagesEntity())
}
