package com.kmp.ollamechat.domain.usecase

import com.kmp.ollamechat.common.AppDispatchers
import com.kmp.ollamechat.domain.datasource.ChatRepository
import com.kmp.ollamechat.domain.datasource.ModelsRepository
import com.kmp.ollamechat.domain.model.ChatRole
import com.kmp.ollamechat.domain.model.OllamaMessage
import com.kmp.ollamechat.domain.model.OllamaModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class LocalDbUseCase(
    private val chatRepository: ChatRepository,
    private val modelsRepository: ModelsRepository,
    private val dispatchers: AppDispatchers
) {
    fun observeMessages(): Flow<List<OllamaMessage>> =
        chatRepository.observeMessages().flowOn(dispatchers.io)

    suspend fun getMessages(): List<OllamaMessage> = withContext(dispatchers.io) {
        chatRepository.getMessages()
    }

    suspend fun insertUserMessage(
        thinking: String? = null,
        content: String,
        imagePath: List<String>
    ) = withContext(Dispatchers.IO)
    {
        chatRepository.insertMessage(
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
        chatRepository.insertMessage(
            role = ChatRole.ASSISTANT,
            thinking = thinking,
            content = content,
            imagePath = imagePath
        )
    }

    suspend fun insertSystemMessage(
        content: String,
    ) = withContext(dispatchers.io) {
        chatRepository.insertMessage(
            role = ChatRole.SYSTEM,
            thinking = null,
            content = content,
            imagePath = emptyList()
        )
    }

    suspend fun updateMessage(message: OllamaMessage) = withContext(dispatchers.io) {
        chatRepository.updateMessage(message)
    }

    suspend fun getModelList(): List<OllamaModel> = withContext(dispatchers.io) {
        modelsRepository.getModelList()
    }

    suspend fun updateModelList(models: List<OllamaModel>) = withContext(dispatchers.io) {
        modelsRepository.updateModelList(models = models)
    }

    fun observeModelList(): Flow<List<OllamaModel>> =
        modelsRepository.observeModelList().flowOn(dispatchers.io)

    suspend fun selectModel(modelName: String) = withContext(dispatchers.io) {
        modelsRepository.selectModel(modelName)
    }

    suspend fun getSelectedModel() = modelsRepository.getModelList().firstOrNull { it.isSelected }
}
