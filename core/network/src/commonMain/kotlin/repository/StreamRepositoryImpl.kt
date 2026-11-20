package com.kmp.ollamechat.core.network.repository

import com.kmp.ollamechat.core.network.client.StreamOllamaClient
import com.kmp.ollamechat.core.network.client.model.OllamaMessage
import com.kmp.ollamechat.core.network.repository.model.StreamOllamaEvent
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
    override fun sendPrompt(ollamaMessages: List<OllamaMessage>): Flow<StreamOllamaEvent> {
            return streamOllamaClient.streamChat(
                serverUrl = settings.getServerUrl(),
                ollamaMessages = ollamaMessages,
                modelId = settings.getCurrentModel().id
            ).flowOn(dispatcher)
    }
}
