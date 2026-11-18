package com.kmp.ollamechat.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kmp.ollamechat.chat.model.ChatMessageDto
import com.kmp.ollamechat.chat.model.ChatMessageUi
import com.kmp.ollamechat.client.model.ChatRole
import com.kmp.ollamechat.repository.Settings
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
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.TimeSource

class ChatViewModel(
    private val streamUseCase: StreamUseCase,
    private val localUseCase: LocalUseCase,
    private val chatImageStorage: ChatImageStorage,
    private val textSpeaker: TextSpeaker,
    private val settings: Settings
) : ViewModel() {

    private val _messages = MutableStateFlow<List<ChatMessageUi>>(emptyList())
    val messages: StateFlow<List<ChatMessageUi>> = _messages.asStateFlow()

    private val _isStreaming = MutableStateFlow(false)
    val isStreaming: StateFlow<Boolean> = _isStreaming.asStateFlow()

    private val _isSpeaking = MutableStateFlow(false)
    private var streamJob: Job? = null

    init {
        viewModelScope.launch {
            localUseCase.observeMessages()
                .conflate()
                .collect { chatMessages ->
                    val chatMessageUi = chatMessages.map { item ->
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

        streamJob?.cancel()
        _isStreaming.value = true

        streamJob = viewModelScope.launch(Dispatchers.Main.immediate) {
            val imageName = file?.let { chatImageStorage.saveImage(it.readBytes()) }

            localUseCase.insertMessage(
                ChatMessageDto(
                    role = ChatRole.USER,
                    thinking = null,
                    content = prompt,
                    imagePath = imageName
                )
            )

            val assistantMessageId = localUseCase.insertMessage(
                ChatMessageDto(
                    role = ChatRole.ASSISTANT,
                    thinking = null,
                    content = ""
                )
            )

            streamAssistantResponse(
                messageId = assistantMessageId,
                prompt = prompt,
                file = file
            )
        }
    }

    private suspend fun streamAssistantResponse(
        messageId: Long,
        prompt: String,
        file: PlatformFile?
    ) {
        val thinkingBuilder = StringBuilder()
        val contentBuilder = StringBuilder()
        val timeSource = TimeSource.Monotonic
        var lastSaveMark = timeSource.markNow()

        try {
            streamUseCase.sendPrompt(
                role = ChatRole.USER,
                content = prompt,
                imageBytes = file?.prepareImageForOllama()
            ).collect { event ->
                when (event) {
                    is StreamOllamaEvent.Token -> {
                        contentBuilder.append(event.text)
                        if (lastSaveMark.elapsedNow() >= settings.getIntervalForSaveMessageToDb()) {
                            lastSaveMark = timeSource.markNow()
                            saveToDb(messageId, thinkingBuilder, contentBuilder)
                        }
                    }

                    is StreamOllamaEvent.Thinking -> {
                        thinkingBuilder.append(event.text)
                        if (lastSaveMark.elapsedNow() >= settings.getIntervalForSaveMessageToDb()) {
                            lastSaveMark = timeSource.markNow()
                            saveToDb(messageId, thinkingBuilder, contentBuilder)
                        }
                    }

                    is StreamOllamaEvent.Error -> {
                        localUseCase.insertMessage(
                            ChatMessageDto(
                                role = ChatRole.SYSTEM,
                                content = "Ошибка: ${event.message}"
                            )
                        )
                        Logger.d(event.message)
                    }

                    is StreamOllamaEvent.Done -> Unit
                }
            }
        } finally {
            _isStreaming.value = false
            // Save last word
            withContext(NonCancellable) {
                saveToDb(messageId, thinkingBuilder, contentBuilder)
            }
        }
    }

    private suspend fun saveToDb(
        id: Long,
        thinkingBuilder: StringBuilder,
        contentBuilder: StringBuilder
    ) {
        localUseCase.updateMessage(
            ChatMessageDto(
                id = id,
                role = ChatRole.ASSISTANT,
                thinking = thinkingBuilder.toString().ifEmpty { null },
                content = contentBuilder.toString()
            )
        )
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
        streamJob?.cancel()
    }

    override fun onCleared() {
        streamJob?.cancel()
        textSpeaker.shutdown()
        super.onCleared()
    }
}
