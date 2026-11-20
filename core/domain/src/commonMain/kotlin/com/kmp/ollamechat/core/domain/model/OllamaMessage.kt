package  com.kmp.ollamechat.core.domain.model

import com.kmp.ollamechat.core.domain.model.ChatRole

data class OllamaMessage(
    val id: Long = 0L,
    val role: ChatRole,
    val content: String,
    val thinking: String? = null,
    val images: List<String>? = null
)
