package com.kmp.ollamechat.domain.model

sealed interface StreamOllamaEvent {
    data class Thinking(val text: String) : StreamOllamaEvent
    data class Token(val text: String) : StreamOllamaEvent
    data class Done(val evalCount: Int?) : StreamOllamaEvent
    data class Error(val message: String, val cause: Throwable? = null) : StreamOllamaEvent
}
