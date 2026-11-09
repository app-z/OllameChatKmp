package com.example.chatkmpdontshareui.viewModel

sealed interface ChatUIState {
    data class ChatMessage(val message: String) : ChatUIState
    data class Error(val error: String) : ChatUIState
    object Idle: ChatUIState
}
