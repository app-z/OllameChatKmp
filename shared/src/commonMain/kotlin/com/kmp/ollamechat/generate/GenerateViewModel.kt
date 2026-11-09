package com.kmp.ollamechat.generate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kmp.ollamechat.useCase.GenerateUseCase
import com.example.chatkmpdontshareui.viewModel.ChatUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GenerateViewModel(
    val generateUseCase: GenerateUseCase
) : ViewModel() {

    private val _messages = MutableStateFlow<ChatUIState>(ChatUIState.Idle)
    val messages: StateFlow<ChatUIState> = _messages

    fun sendMessage(text: String) {
        viewModelScope.launch {
            generateUseCase.sendPrompt(text)
                .onSuccess { result ->
                    _messages.emit(ChatUIState.ChatMessage(result))
                }.onFailure { error ->
                    _messages.emit(ChatUIState.Error(error.message ?: "Unknown Error!"))
                }
        }
    }
}