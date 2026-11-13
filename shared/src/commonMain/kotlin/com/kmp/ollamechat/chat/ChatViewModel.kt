package com.kmp.ollamechat.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kmp.ollamechat.chat.model.ChatMessage
import com.kmp.ollamechat.chat.model.ChatRole
import com.kmp.ollamechat.client.model.ChatRequest
import com.kmp.ollamechat.repository.model.StreamOllamaEvent
import com.kmp.ollamechat.useCase.LocalUseCase
import com.kmp.ollamechat.useCase.StreamUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.concurrent.atomics.AtomicLong
import kotlin.concurrent.atomics.ExperimentalAtomicApi
import kotlin.concurrent.atomics.incrementAndFetch

@OptIn(ExperimentalAtomicApi::class)
class ChatViewModel(
    private val streamUseCase: StreamUseCase,
    private val localUseCase: LocalUseCase
) : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _isStreaming = MutableStateFlow(false)
    val isStreaming: StateFlow<Boolean> = _isStreaming.asStateFlow()

    val chunkList = mutableListOf<String>()

    private var streamJob: Job? = null

    init {
        viewModelScope.launch {
            localUseCase.observeMessages().collect { chatMessages ->
                _messages.emit(chatMessages)
            }
        }

    }

    fun send(text: String) {

        _isStreaming.value = true
        chunkList.clear()

        streamJob = viewModelScope.launch(Dispatchers.Main.immediate) {
            localUseCase.insertMessage(
                ChatMessage(
                    role = ChatRole.USER,
                    thinking = null,
                    content = text
                )
            )

            val id = localUseCase.insertMessage(
                ChatMessage(
                    role = ChatRole.ASSISTANT,
                    thinking = null,
                    content = ""
                )
            )

            streamUseCase.sendPrompt(
                role = ChatRole.USER,
                content = text
            )
                .collect { event ->
                    when (event) {
                        is StreamOllamaEvent.Token -> {
                            chunkList.add(event.text)
                            localUseCase.updateMessage(
                                ChatMessage(
                                    id = id,
                                    role = ChatRole.ASSISTANT,
                                    thinking = null,
                                    content = chunkList.joinToString("")
                                )
                            )
                        }

                        is StreamOllamaEvent.Thinking -> {
                            chunkList.add(event.text)
                            localUseCase.updateMessage(
                                ChatMessage(
                                    id = id,
                                    role = ChatRole.ASSISTANT,
                                    thinking =  chunkList.joinToString(""),
                                    content = ""
                                )
                            )
                        }


                        is StreamOllamaEvent.Error -> {
                            _isStreaming.value = false
                            _messages.update { list ->
                                if (list.isEmpty()) return@update list
                                val last = list.last()
                                list.dropLast(1) + last.copy(content = last.content + "\n[ошибка: ${event.message}]")
                            }
                        }

                        is StreamOllamaEvent.Done -> {
                            _isStreaming.value = false
                        }
                    }

                }
        }
    }

    fun stop() {
        streamJob?.cancel()   // отмена Flow → закрытие соединения → стоп генерации
        _isStreaming.value = false
    }

    override fun onCleared() {
        streamJob?.cancel()
        super.onCleared()
    }
}