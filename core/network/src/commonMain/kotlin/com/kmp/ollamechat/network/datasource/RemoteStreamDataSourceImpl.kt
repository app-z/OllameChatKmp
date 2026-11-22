package com.kmp.ollamechat.network.datasource

import com.kmp.ollamechat.common.AppDispatchers
import com.kmp.ollamechat.common.Settings
import com.kmp.ollamechat.domain.datasource.RemoteStreamDataSource
import com.kmp.ollamechat.domain.model.OllamaMessage
import com.kmp.ollamechat.domain.model.StreamOllamaEvent
import com.kmp.ollamechat.network.client.StreamOllamaClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

class RemoteStreamDataSourceImpl(
    private val streamOllamaClient: StreamOllamaClient,
    private val settings: Settings,
    private val dispatchers: AppDispatchers
) : RemoteStreamDataSource {
    override fun sendPrompt(ollamaMessages: List<OllamaMessage>): Flow<StreamOllamaEvent> {
            return streamOllamaClient.streamChat(
                serverUrl = settings.getServerUrl(),
                ollamaMessages = ollamaMessages,
                modelId = settings.getCurrentModel().id
            ).flowOn(dispatchers.io)
    }
}
