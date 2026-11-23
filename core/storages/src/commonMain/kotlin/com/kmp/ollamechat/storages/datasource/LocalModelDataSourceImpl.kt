package com.kmp.ollamechat.storages.datasource

import com.kmp.ollamechat.domain.datasource.LocalModelDataSource
import com.kmp.ollamechat.domain.model.OllamaModel
import com.kmp.ollamechat.storages.database.ModelDao
import com.kmp.ollamechat.storages.database.toDomain
import com.kmp.ollamechat.storages.database.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalModelDataSourceImpl(
    private val modelDao: ModelDao,
) : LocalModelDataSource {
    override suspend fun getModelList(): List<OllamaModel> {
        val modelsEntry =  modelDao.getModels()
        return modelsEntry.toDomain()
    }

    override suspend fun updateModelList(models: List<OllamaModel>) {
        modelDao.replaceAll(models.toEntity())
    }

    override fun observeModelList(): Flow<List<OllamaModel>> = modelDao.observeModelList().map {
        modelsEntities -> modelsEntities.toDomain()
    }

    override suspend fun selectModel(modelName: String) {
        modelDao.selectExclusive(modelName)
    }

}
