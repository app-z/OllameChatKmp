package com.kmp.ollamechat.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kmp.ollamechat.chat.model.ChatMessageUi
import com.kmp.ollamechat.chat.model.toChatMessageUi
import com.kmp.ollamechat.core.domain.model.ChatRole
import com.kmp.ollamechat.core.domain.model.StreamOllamaEvent
import com.kmp.ollamechat.core.usecase.LocalUseCase
import com.kmp.ollamechat.core.usecase.StreamUseCase
import com.kmp.ollamechat.util.ChatImageStorage
import com.kmp.ollamechat.util.Logger
import com.kmp.ollamechat.util.TextSpeaker
import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.ImageFormat
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.compressImage
import io.github.vinceglb.filekit.readBytes
import io.github.vinceglb.filekit.size
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
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
import kotlinx.coroutines.withContext
import ollamechat.shared.generated.resources.Res
import ollamechat.shared.generated.resources.describe_picture
import org.jetbrains.compose.resources.getString

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
                    imageFile = chatImageStorage.getImageFile(item.images?.firstOrNull())
                )
            }
        }

    val messagesFlow: StateFlow<List<ChatMessageUi>> = combine(
        dbMessagesFlow
            .conflate(),
        messageUpdateStrategy.currentMessage
            .onStart {
                emit(null) // start emit list first for of combine
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

        streamJob?.cancel()
        _isStreaming.value = true
        messageUpdateStrategy.clean()

        streamJob = viewModelScope.launch(Dispatchers.Main.immediate) {
            val prompt = if (text.isBlank() && file != null)
                getString(Res.string.describe_picture) else text

            val imageName = file?.let { chatImageStorage.saveImage(it.readBytes()) }

            localUseCase.insertUserMessage(
                content = prompt,
                imagePath = if (imageName.isNullOrEmpty()) emptyList() else listOf(imageName)
            )

            val assistantMessageId = localUseCase.insertAssistantMessage(
                content = ""
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
                        localUseCase.insertSystemMessage(
                            content = "Ошибка: ${event.message}"
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

    /**
     * Читает файл и сжимает его, если размер превышает заданный порог.
     * По умолчанию порог — 500 КБ (512 * 1024 байт).
     */
    suspend fun PlatformFile.prepareImageForOllama(
        maxFileSizeBytes: Long = 512 * 1024L,
        quality: Int = 80,
        maxDimension: Int = 1024
    ): ByteArray = withContext(Dispatchers.IO) {
        // 1. Проверяем размер файла на диске до полного чтения
        val fileSize = size()
        val rawBytes = readBytes()

        // 2. Если картинка уже маленькая (проверяем и размер файла, и длину массива), возвращаем как есть
        val actualSize = if (fileSize > 0) fileSize else rawBytes.size.toLong()
        if (actualSize <= maxFileSizeBytes) {
            return@withContext rawBytes
        }

        // 3. Сжимаем тяжелое изображение и уменьшаем его до maxDimension по длинной стороне
        val compressedBytes = FileKit.compressImage(
            bytes = rawBytes,
            quality = quality, // 0..100
            maxWidth = maxDimension,
            maxHeight = maxDimension,
            imageFormat = ImageFormat.JPEG
        )

        // 4. Защита на случай, если после перекодирования в JPEG файл внезапно стал больше (редко, но бывает для PNG)
        if (compressedBytes.size < rawBytes.size) {
            compressedBytes
        } else {
            rawBytes
        }
    }

}
