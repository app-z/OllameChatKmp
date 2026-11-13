package com.kmp.ollamechat.client.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OllamaChatRequest(
    val model: String,
    val messages: List<OllamaMessage>,
    val stream: Boolean = true,
    @SerialName("keep_alive")
    val keepAlive: String = "30m",
    val options: OllamaOptions? = null
)
