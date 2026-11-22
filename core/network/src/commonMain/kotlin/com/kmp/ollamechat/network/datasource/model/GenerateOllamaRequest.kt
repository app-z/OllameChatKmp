package com.kmp.ollamechat.network.datasource.model

import kotlinx.serialization.Serializable

@Serializable
data class GenerateOllamaRequest(
    val model: String,
    val prompt: String,
    val stream: Boolean
)
