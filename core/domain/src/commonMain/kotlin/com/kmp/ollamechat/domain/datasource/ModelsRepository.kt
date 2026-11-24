package com.kmp.ollamechat.domain.datasource

import com.kmp.ollamechat.domain.model.OllamaModel
import kotlinx.coroutines.flow.Flow

interface ModelsRepository {

    suspend fun getModelList(): List<OllamaModel>

    suspend fun updateModelList(models: List<OllamaModel>)

    fun observeModelList(): Flow<List<OllamaModel>>

    suspend fun selectModel(modelName: String)
}
