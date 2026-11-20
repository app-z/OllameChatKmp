package  com.kmp.ollamechat.core.network.client.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// Один чанк NDJSON-потока
@Serializable
data class OllamaChatChunk(
    val model: String? = null,
    val message: OllamaMessage? = null,
    val done: Boolean = false,
    @SerialName("done_reason")
    val doneReason: String? = null,
    @SerialName("eval_count")
    val evalCount: Int? = null,
    val error: String? = null
)