package  com.kmp.ollamechat.core.network.client.model

import com.kmp.ollamechat.core.network.repository.model.ChatMessageDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OllamaChatRequest(
    val model: String,
    val messages: List<ChatMessageDto>,
    val stream: Boolean = true,
    @SerialName("keep_alive")
    val keepAlive: String = "30m",
    val options: OllamaOptions? = null
)
