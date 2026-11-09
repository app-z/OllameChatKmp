package com.kmp.ollamechat.chatscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chatkmpdontshareui.useCase.OllamaUseCase
import com.example.chatkmpdontshareui.viewModel.ChatUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ChatViewModel(
    val ollamaUseCase: OllamaUseCase
) : ViewModel() {

    private val _messages = MutableStateFlow<ChatUIState>(ChatUIState.Idle)
    val messages: StateFlow<ChatUIState> = _messages

    fun sendMessage(text: String) {
        viewModelScope.launch {
            ollamaUseCase.sendPrompt(text)
                .onSuccess { result ->
                    _messages.emit(ChatUIState.ChatMessage(result))
                }.onFailure { error ->
                    _messages.emit(ChatUIState.Error(error.message ?: "Unknown Error!"))
                }
        }
    }
}