package com.kmp.ollamechat.network.datasource

import com.kmp.ollamechat.common.AiModelsSettings
import com.kmp.ollamechat.common.AppDispatchers
import com.kmp.ollamechat.domain.datasource.RemoteStreamDataSource
import com.kmp.ollamechat.domain.model.OllamaMessage
import com.kmp.ollamechat.domain.model.OllamaModel
import com.kmp.ollamechat.domain.model.StreamOllamaEvent
import com.kmp.ollamechat.network.client.OllamaModelClient
import com.kmp.ollamechat.network.client.ServerSettings
import com.kmp.ollamechat.network.client.StreamOllamaClient
import com.kmp.ollamechat.network.client.mapper.toOllamaModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class RemoteStreamDataSourceImpl(
    private val streamOllamaClient: StreamOllamaClient,
    private val ollamaModelClient: OllamaModelClient,
    private val aiModelsSettings: AiModelsSettings,
    private val serverSettings: ServerSettings,
    private val dispatchers: AppDispatchers
) : RemoteStreamDataSource {
    override fun sendPrompt(ollamaMessages: List<OllamaMessage>): Flow<StreamOllamaEvent> {
            return streamOllamaClient.streamChat(
                serverUrl = serverSettings.getServerUrl(),
                ollamaMessages = ollamaMessages,
                modelId = aiModelsSettings.getCurrentModel().id
            ).flowOn(dispatchers.io)
    }

    override suspend fun getModels(): Result<List<OllamaModel>> {
        return withContext(dispatchers.io) {
            ollamaModelClient.getModelsTag(
                serverSettings.getServerUrl()
            ).map { dto -> dto.map { it.toOllamaModel() } }
        }
    }
}
