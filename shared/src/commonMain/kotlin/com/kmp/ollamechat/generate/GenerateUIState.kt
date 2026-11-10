package com.kmp.ollamechat.generate

sealed interface GenerateUIState {
    data class GenerateMessage(val message: String) : GenerateUIState
    data class Error(val error: String) : GenerateUIState
    object Idle: GenerateUIState
}
