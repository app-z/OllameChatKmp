package com.kmp.ollamechat.generate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kmp.ollamechat.useCase.GenerateUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GenerateViewModel(
    val generateUseCase: GenerateUseCase
) : ViewModel() {

    private val _messages = MutableStateFlow<GenerateUIState>(GenerateUIState.Idle)
    val messages: StateFlow<GenerateUIState> = _messages

    fun sendMessage(text: String) {
        viewModelScope.launch {
            generateUseCase.sendPrompt(text)
                .onSuccess { result ->
                    _messages.emit(GenerateUIState.GenerateMessage(result))
                }.onFailure { error ->
                    _messages.emit(GenerateUIState.Error(error.message ?: "Unknown Error!"))
                }
        }
    }
}