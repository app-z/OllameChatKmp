package com.kmp.ollamechat.repository

import com.kmp.ollamechat.chat.model.ChatMessageDto
import kotlinx.coroutines.flow.Flow

interface LocalRepository{
    suspend fun updateMessage(chatMessageDto: ChatMessageDto)

    suspend fun getMessages(): List<ChatMessageDto>

    fun observeMessages() : Flow<List<ChatMessageDto>>

    suspend fun insertMessage(chatMessageDto: ChatMessageDto): Long
}