package com.kmp.ollamechat.repository

import com.kmp.ollamechat.chat.model.ChatMessage
import com.kmp.ollamechat.database.MessagesDao
import com.kmp.ollamechat.database.MessagesEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class LocalRepositoryImpl(
    val messagesDao: MessagesDao
) : LocalRepository {
    override suspend fun getMessages(): List<ChatMessage> {
        return messagesDao.getAllMessages().first().map { item ->
            ChatMessage(
                item.id,
                content = item.content,
                role = item.role,
                thinking = item.thinking,
            )
        }
    }

    override fun observeMessages(): Flow<List<ChatMessage>> =
        messagesDao.getAllMessages().map { entitiesList ->
            entitiesList.map { item ->
                ChatMessage(
                    id = item.id,
                    content = item.content,
                    role = item.role,
                    thinking = item.thinking,
                )
            }
        }

    override suspend fun insertMessage(message: ChatMessage): Long = messagesDao.insertMessage(
        MessagesEntity(
            content = message.content,
            role = message.role,
            thinking = message.thinking,
        )
    )


    override suspend fun updateMessage(message: ChatMessage) = messagesDao.updateMessage(
        MessagesEntity(
            id =  message.id,
            content = message.content,
            role = message.role,
            thinking = message.thinking,
        )
    )
}
