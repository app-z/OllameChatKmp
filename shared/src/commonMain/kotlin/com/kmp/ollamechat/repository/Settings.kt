package com.kmp.ollamechat.repository

import com.example.chatkmpdontshareui.repository.model.ModelOption

class Settings {
    private val serverUrl = "http://192.168.1.215:11434"

    fun getServerUrl() = serverUrl

    private val availableModels = listOf(
        ModelOption("gemma4:e4b-mlx", "gemma4:e4b-mlx"),
        ModelOption("GigaChat 3 (10B)", "ai-sage/GigaChat3-10B-A1.8B"),
        ModelOption("GigaChat 2 Max", "GigaChat/GigaChat-2-Max"),
        ModelOption("DeepSeek V4 Pro", "deepseek-ai/DeepSeek-V4-Pro"),
        ModelOption("Qwen 3 Coder", "Qwen/Qwen3-Coder-Next"),
        ModelOption("GLM-4.7", "zai-org/GLM-4.7"),
        ModelOption("Kimi K2.6", "moonshotai/Kimi-K2.6"),
        ModelOption("MiniMax M2.5", "MiniMaxAI/MiniMax-M2.5")
    )

    fun getAvailableModels() = availableModels

    private var currentModel = getAvailableModels().first()

    fun getCurrentModel() = currentModel

}
