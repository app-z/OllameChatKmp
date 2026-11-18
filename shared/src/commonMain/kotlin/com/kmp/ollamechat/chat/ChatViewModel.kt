package com.kmp.ollamechat.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kmp.ollamechat.chat.model.ChatMessageDto
import com.kmp.ollamechat.chat.model.ChatMessageUi
import com.kmp.ollamechat.client.model.ChatRole
import com.kmp.ollamechat.database.toChatMessageUi
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ChatViewModel(
    private val streamUseCase: StreamUseCase,
    private val localUseCase: LocalUseCase,
    private val chatImageStorage: ChatImageStorage,
    private val messageUpdateStrategy: MessageUpdateStrategy,
    private val textSpeaker: TextSpeaker,
) : ViewModel() {
    private val _isStreaming = MutableStateFlow(false)
    val isStreaming: StateFlow<Boolean> = _isStreaming.asStateFlow()

    private val _isSpeaking = MutableStateFlow(false)
    private var streamJob: Job? = null

    private val dbMessagesFlow: Flow<List<ChatMessageUi>> = localUseCase.observeMessages()
        .map { db ->
            db.map { item ->
                ChatMessageUi(
                    id = item.id,
                    role = item.role,
                    content = item.content,
                    thinking = item.thinking,
                    imageFile = chatImageStorage.getImageFile(item.imagePath)
                )
            }
        }

    val messagesFlow: StateFlow<List<ChatMessageUi>> = combine(
        dbMessagesFlow
            .conflate(),
        messageUpdateStrategy.currentMessage
            .conflate()
            .onStart {
                emit(null) // start emit list because of combine
            }
    ) { dbUi, current ->
        if (current == null) {
            dbUi
        } else {
            val currentUi = current.toChatMessageUi().copy(
                imageFile = chatImageStorage.getImageFile(current.imagePath)
            )
            dbUi.filterNot { it.id == currentUi.id } + currentUi
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    )

    fun send(text: String, file: PlatformFile?) {
        val prompt = if (text.isBlank() && file != null) "Опиши картинку" else text

        streamJob?.cancel()
        _isStreaming.value = true
        messageUpdateStrategy.clean()

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

        try {
            streamUseCase.sendPrompt(
                role = ChatRole.USER,
                content = prompt,
                imageBytes = file?.prepareImageForOllama()
            ).collect { event ->
                when (event) {
                    is StreamOllamaEvent.Token -> {

                        messageUpdateStrategy.updateContent(
                            messageId = messageId,
                            content = event.text
                        )
                    }

                    is StreamOllamaEvent.Thinking -> {

                        messageUpdateStrategy.updateThinking(
                            messageId = messageId,
                            thinking = event.text
                        )
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
            messageUpdateStrategy.saveLastWordToDbAndClean(
                messageId = messageId
            )
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
        streamJob?.cancel()
    }

    override fun onCleared() {
        streamJob?.cancel()
        textSpeaker.shutdown()
        super.onCleared()
    }
}
