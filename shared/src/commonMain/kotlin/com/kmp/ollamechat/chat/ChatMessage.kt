package com.kmp.ollamechat.chat

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlin.jvm.Transient


data class ChatMessage(
    val id: Long,
    val role: String,          // "system" | "user" | "assistant" | "tool"
    val content: String,
    val thinking: String? = null,
    val images: List<String>? = null
)

@Serializable
data class OllamaChatRequest(
    val model: String,
    val messages: List<ChatRequest>,
    val stream: Boolean = true,
    @SerialName("keep_alive")
    val keepAlive: String = "30m",
    val options: OllamaOptions? = null
)

@Serializable
data class OllamaOptions(
    val temperature: Double? = null,
    @SerialName("num_predict") val numPredict: Int? = null
)

// Один чанк NDJSON-потока
@Serializable
data class OllamaChatChunk(
    val model: String? = null,
    val message: ChatRequest? = null,
    val done: Boolean = false,
    @SerialName("done_reason") val doneReason: String? = null,
    @SerialName("eval_count") val evalCount: Int? = null,
    val error: String? = null
)