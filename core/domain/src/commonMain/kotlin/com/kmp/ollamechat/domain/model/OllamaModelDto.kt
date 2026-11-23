package com.kmp.ollamechat.network.client.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OllamaTagsResponseDto(
    val models: List<OllamaModelDto> = emptyList()
)

@Serializable
data class OllamaModelDto(
    val name: String,
    val model: String,
    @SerialName("modified_at") val modifiedAt: String,
    val size: Long,
    val digest: String,
    val details: OllamaModelDetailsDto? = null
)

@Serializable
data class OllamaModelDetailsDto(
    val format: String? = null,
    val family: String? = null,
    val families: List<String>? = null,
    @SerialName("parameter_size") val parameterSize: String? = null,
    @SerialName("quantization_level") val quantizationLevel: String? = null
)
