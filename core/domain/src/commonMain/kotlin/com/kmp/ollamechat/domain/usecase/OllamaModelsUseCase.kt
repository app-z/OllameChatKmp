package com.kmp.ollamechat.domain.usecase

import com.kmp.ollamechat.domain.datasource.OllamaRepository

class OllamaModelsUseCase(private val dataSource: OllamaRepository) {
    suspend fun getOllamaModels() = dataSource.getModels()
}
