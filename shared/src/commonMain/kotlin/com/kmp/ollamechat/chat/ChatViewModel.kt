package com.kmp.ollamechat.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kmp.ollamechat.chat.model.ChatMessageDto
import com.kmp.ollamechat.chat.model.ChatMessageUi
import com.kmp.ollamechat.client.model.ChatRole
import com.kmp.ollamechat.repository.model.StreamOllamaEvent
import com.kmp.ollamechat.useCase.LocalUseCase
import com.kmp.ollamechat.useCase.StreamUseCase
import com.kmp.ollamechat.useCase.prepareImageForOllama
import com.kmp.ollamechat.util.ChatImageStorage
import com.kmp.ollamechat.util.Logger
import com.kmp.ollamechat.util.TextSpeaker
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.readBytes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel(
    private val streamUseCase: StreamUseCase,
    private val localUseCase: LocalUseCase,
    private val chatImageStorage: ChatImageStorage,
    private val textSpeaker: TextSpeaker
) : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatMessageUi>>(emptyList())
    val messages: StateFlow<List<ChatMessageUi>> = _messages.asStateFlow()

    private val _isStreaming = MutableStateFlow(false)
    val isStreaming: StateFlow<Boolean> = _isStreaming.asStateFlow()

    private val _isSpeaking = MutableStateFlow(false)
    private var streamJob: Job? = null

    init {
        viewModelScope.launch {
            localUseCase.observeMessages().collect { chatMessages ->
                val chatMessageUi =
                    chatMessages.map { item ->
                        ChatMessageUi(
                            id = item.id,
                            role = item.role,
                            content = item.content,
                            thinking = item.thinking,
                            imageFile = chatImageStorage.getImageFile(item.imagePath)
                        )
                    }

                _messages.emit(chatMessageUi)
            }
        }
    }

    fun send(text: String, file: PlatformFile?) {
        val prompt = if (text.isBlank() && file != null) "Опиши картинку" else text

        _isStreaming.value = true

        streamJob = viewModelScope.launch(Dispatchers.Main.immediate) {
            var imageName: String? = null
            if (file != null) {
                imageName = chatImageStorage.saveImage(file.readBytes())
            }

            localUseCase.insertMessage(
                ChatMessageDto(
                    role = ChatRole.USER,
                    thinking = null,
                    content = prompt,
                    imagePath = imageName
                )
            )

            val id = localUseCase.insertMessage(
                ChatMessageDto(
                    role = ChatRole.ASSISTANT,
                    thinking = null,
                    content = ""
                )
            )

            val thinkingBuilder = StringBuilder()
            val contentBuilder = StringBuilder()

            streamUseCase.sendPrompt(
                role = ChatRole.USER,
                content = prompt,
                file?.prepareImageForOllama()
            )
                .collect { event ->
                    when (event) {
                        is StreamOllamaEvent.Token -> {
                            contentBuilder.append(event.text)
                            localUseCase.updateMessage(
                                ChatMessageDto(
                                    id = id,
                                    role = ChatRole.ASSISTANT,
                                    thinking = thinkingBuilder.toString(),
                                    content = contentBuilder.toString()
                                )
                            )
                        }

                        is StreamOllamaEvent.Thinking -> {
                            thinkingBuilder.append(event.text)
                            localUseCase.updateMessage(
                                ChatMessageDto(
                                    id = id,
                                    role = ChatRole.ASSISTANT,
                                    thinking = thinkingBuilder.toString().ifEmpty { null },
                                    content = contentBuilder.toString()
                                )
                            )
                        }

                        is StreamOllamaEvent.Error -> {
                            _isStreaming.value = false
                            localUseCase.insertMessage(
                                ChatMessageDto(
                                    role = ChatRole.SYSTEM,
                                    content = "Ошибка: ${event.message}"
                                )
                            )
                            Logger.d( event.message)
                        }

                        is StreamOllamaEvent.Done -> {
                            _isStreaming.value = false
                        }
                    }

                }
        }
    }

    fun speak(tts: String) {
        _isSpeaking.value = !_isSpeaking.value
        if (_isSpeaking.value) {
            textSpeaker.speak(tts)
        } else {
            textSpeaker.stop()
        }
    }

    fun stop() {
        streamJob?.cancel()   // отмена Flow → закрытие соединения → стоп генерации
        _isStreaming.value = false
    }

    override fun onCleared() {
        streamJob?.cancel()
        textSpeaker.shutdown()
        super.onCleared()
    }
}