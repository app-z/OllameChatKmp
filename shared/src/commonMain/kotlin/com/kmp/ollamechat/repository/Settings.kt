package com.kmp.ollamechat.repository

import com.kmp.ollamechat.repository.model.ModelOption
import kotlin.time.Duration.Companion.milliseconds

class Settings {
    private val url = "http://192.168.1.215:11434"

//    private val url = "http://192.168.1.216:11434"

    fun getServerUrl() = url

    private val availableModels = listOf(
        ModelOption("gemma4:26b", "gemma4:26b"),
        ModelOption("gemma4-fast:latest", "gemma4-fast:latest"),
        ModelOption("gemma4:e4b-mlx", "gemma4:e4b-mlx"),
        ModelOption("gemma4:12b-mlx", "gemma4:e4b-mlx"),
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

    fun getSaveMessageToDbInterval() = 250.milliseconds

}
