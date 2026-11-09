package com.kmp.ollamechat.repository

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class RepositoryImpl(
    val settings: Settings,
    val ollamaClient: OllamaClient,
    val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : Repository {
    override suspend fun sendPrompt(
        prompt: String
    ): Result<String> {
        return withContext(dispatcher) {
            ollamaClient.sendPrompt(settings.getServerUrl(), prompt)
        }
    }
}