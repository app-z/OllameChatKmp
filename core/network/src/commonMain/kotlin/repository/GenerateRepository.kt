package com.kmp.ollamechat.core.network.repository

interface GenerateRepository {
    suspend fun sendPrompt(prompt: String): Result<String>
}
