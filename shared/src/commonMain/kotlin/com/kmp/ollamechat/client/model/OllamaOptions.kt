package com.kmp.ollamechat.client.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OllamaOptions(
    val temperature: Double? = null,
    @SerialName("num_predict") val numPredict: Int? = null
)
