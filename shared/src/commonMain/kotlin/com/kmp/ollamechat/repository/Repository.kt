package com.kmp.ollamechat.repository

interface Repository {
    suspend fun sendPrompt(prompt: String): Result<String>
}