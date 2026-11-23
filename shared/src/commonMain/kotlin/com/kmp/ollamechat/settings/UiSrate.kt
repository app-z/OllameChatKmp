package com.kmp.ollamechat.settings

import com.kmp.ollamechat.domain.model.OllamaModel


sealed interface UiState {
    data class Success(val ollamaModel: List<OllamaModel>) : UiState
    object Loading : UiState
    data class Error(val ex: Throwable) : UiState
}