package com.kmp.ollamechat.repository

interface GenerateRepository {
    suspend fun sendPrompt(prompt: String): Result<String>
}
