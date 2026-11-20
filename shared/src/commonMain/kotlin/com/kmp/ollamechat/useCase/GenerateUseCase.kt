package com.kmp.ollamechat.useCase

import com.kmp.ollamechat.core.network.repository.GenerateRepository

class GenerateUseCase(val generateRepository: GenerateRepository) {

    suspend fun sendPrompt(prompt: String): Result<String>{
        return generateRepository.sendPrompt(prompt)
    }
}
