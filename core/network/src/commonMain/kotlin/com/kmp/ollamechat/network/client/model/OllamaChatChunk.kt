package com.kmp.ollamechat.network.client.model

import com.kmp.ollamechat.network.datasource.model.ChatMessageDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OllamaChatChunk(
    val model: String? = null,
    val message: ChatMessageDto? = null,
    val done: Boolean = false,
    @SerialName("done_reason")
    val doneReason: String? = null,
    @SerialName("eval_count")
    val evalCount: Int? = null,
    val error: String? = null
)