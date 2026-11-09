package com.kmp.ollamechat.repository

import com.kmp.ollamechat.chat.ChatRequest
import com.kmp.ollamechat.client.StreamOllamaClient
import com.kmp.ollamechat.repository.model.StreamOllamaEvent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class StreamRepositoryImpl(
    private val streamOllamaClient: StreamOllamaClient,
    private val settings: Settings,
    val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : StreamRepository {
    override fun sendPrompt(messages: List<ChatRequest>): Flow<StreamOllamaEvent> {
            return streamOllamaClient.streamChat(
                serverUrl = settings.getServerUrl(),
                messages = messages,
                modelId = settings.getCurrentModel().id
            ).flowOn(dispatcher)
    }
}
