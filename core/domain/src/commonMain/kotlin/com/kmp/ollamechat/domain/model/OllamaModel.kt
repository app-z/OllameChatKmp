package com.kmp.ollamechat.domain.model

data class OllamaModel(
//    val id: Long = 0L,
    val name: String,
    val model: String,
    val size: Long,
    val digest: String,
    val modifiedAt: String,
    val isSelected: Boolean = false
)
