package com.kmp.ollamechat.domain.usecase

import com.kmp.ollamechat.domain.datasource.RemoteStreamDataSource

class OllamaModelsUseCase(private val dataSource: RemoteStreamDataSource) {
    suspend fun getOllamaModels() = dataSource.getModels()
}
