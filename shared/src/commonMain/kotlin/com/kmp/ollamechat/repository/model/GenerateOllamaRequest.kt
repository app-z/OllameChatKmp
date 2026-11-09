package com.kmp.ollamechat.repository.model

import kotlinx.serialization.Serializable

@Serializable
data class GenerateOllamaRequest(
    val model: String,
    val prompt: String,
    val stream: Boolean
)
