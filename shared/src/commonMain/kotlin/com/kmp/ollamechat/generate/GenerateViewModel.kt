package com.kmp.ollamechat.generate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kmp.ollamechat.chat.model.ChatMessage
import com.kmp.ollamechat.chat.model.ChatRole
import com.kmp.ollamechat.useCase.GenerateUseCase
import com.kmp.ollamechat.useCase.LocalUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GenerateViewModel(
    val generateUseCase: GenerateUseCase,
    val localUseCase: LocalUseCase
) : ViewModel() {

    private val _messagesUiState = MutableStateFlow<GenerateUIState>(GenerateUIState.Idle)
    val messagesUiState: StateFlow<GenerateUIState> = _messagesUiState.asStateFlow()

    init {
        viewModelScope.launch {
            localUseCase.observeMessages().collect { chatMessage ->
                chatMessage.forEach {
                    _messagesUiState.emit(
                        GenerateUIState.GenerateMessage(
                            message = it.content
                        )
                    )
                }
            }
        }
    }

    fun sendMessage(text: String) {
        viewModelScope.launch {
            generateUseCase.sendPrompt(text)
                .onSuccess { result ->
                    localUseCase.insertMessage(
                        ChatMessage(
                            content = result,
                            thinking = "",
                            role = ChatRole.ASSISTANT
                        )
                    )
//                    _messages.emit(GenerateUIState.GenerateMessage(result))
                }.onFailure { error ->
                    _messagesUiState.emit(GenerateUIState.Error(error.message ?: "Unknown Error!"))
                }
        }
    }
}