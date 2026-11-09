package com.example.chatkmpdontshareui.useCase

import com.kmp.ollamechat.repository.Repository

class OllamaUseCase(val repository: Repository) {

    suspend fun sendPrompt(prompt: String): Result<String>{
        return repository.sendPrompt(prompt)
    }
}
