package com.kmp.ollamechat.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class GenerateRepositoryImpl(
    val settings: Settings,
    val generateOllamaClient: GenerateOllamaClient,
    val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : GenerateRepository {
    override suspend fun sendPrompt(
        prompt: String
    ): Result<String> {
        return withContext(dispatcher) {
            generateOllamaClient.sendPrompt(settings.getServerUrl(), prompt)
        }
    }
}