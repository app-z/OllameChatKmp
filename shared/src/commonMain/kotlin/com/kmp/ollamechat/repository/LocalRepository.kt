package com.kmp.ollamechat.repository

import com.kmp.ollamechat.chat.model.ChatMessage
import com.kmp.ollamechat.database.MessagesEntity
import kotlinx.coroutines.flow.Flow

interface LocalRepository{
    suspend fun updateMessage(chatMessage: ChatMessage)

    suspend fun getMessages(): List<ChatMessage>

    fun observeMessages() : Flow<List<ChatMessage>>

    suspend fun insertMessage(message: ChatMessage): Long
}