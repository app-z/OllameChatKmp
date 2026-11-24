package com.kmp.ollamechat.domain.usecase

import com.kmp.ollamechat.common.AppDispatchers
import com.kmp.ollamechat.domain.datasource.LocalChatDataSource
import com.kmp.ollamechat.domain.datasource.LocalModelDataSource
import com.kmp.ollamechat.domain.model.ChatRole
import com.kmp.ollamechat.domain.model.OllamaMessage
import com.kmp.ollamechat.domain.model.OllamaModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class LocalDbUseCase(
    private val localChatDataSource: LocalChatDataSource,
    private val localModelDataSource: LocalModelDataSource,
    private val dispatchers: AppDispatchers
) {
    fun observeMessages(): Flow<List<OllamaMessage>> =
        localChatDataSource.observeMessages().flowOn(dispatchers.io)

    suspend fun getMessages(): List<OllamaMessage> = withContext(dispatchers.io) {
        localChatDataSource.getMessages()
    }

    suspend fun insertUserMessage(
        thinking: String? = null,
        content: String,
        imagePath: List<String>
    ) = withContext(Dispatchers.IO)
    {
        localChatDataSource.insertMessage(
            role = ChatRole.USER,
            thinking = thinking,
            content = content,
            imagePath = imagePath
        )
    }

    suspend fun insertAssistantMessage(
        thinking: String? = null,
        content: String,
        imagePath: List<String> = emptyList()
    ) = withContext(dispatchers.io) {
        localChatDataSource.insertMessage(
            role = ChatRole.ASSISTANT,
            thinking = thinking,
            content = content,
            imagePath = imagePath
        )
    }

    suspend fun insertSystemMessage(
        content: String,
    ) = withContext(dispatchers.io) {
        localChatDataSource.insertMessage(
            role = ChatRole.SYSTEM,
            thinking = null,
            content = content,
            imagePath = emptyList()
        )
    }

    suspend fun updateMessage(message: OllamaMessage) = withContext(dispatchers.io) {
        localChatDataSource.updateMessage(message)
    }

    suspend fun getModelList(): List<OllamaModel> = withContext(dispatchers.io) {
        localModelDataSource.getModelList()
    }

    suspend fun updateModelList(models: List<OllamaModel>) = withContext(dispatchers.io) {
        localModelDataSource.updateModelList(models = models)
    }

    fun observeModelList(): Flow<List<OllamaModel>> =
        localModelDataSource.observeModelList().flowOn(dispatchers.io)

    suspend fun selectModel(modelName: String) = withContext(dispatchers.io) {
        localModelDataSource.selectModel(modelName)
    }

    suspend fun getSelectedModel() = localModelDataSource.getModelList().firstOrNull { it.isSelected }
}
