package com.kmp.ollamechat.useCase

import com.kmp.ollamechat.client.model.ChatRole
import com.kmp.ollamechat.client.model.OllamaMessage
import com.kmp.ollamechat.repository.StreamRepository
import com.kmp.ollamechat.repository.model.StreamOllamaEvent
import kotlinx.coroutines.flow.Flow
import kotlin.io.encoding.Base64

import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.ImageFormat
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.compressImage
import io.github.vinceglb.filekit.readBytes
import io.github.vinceglb.filekit.size
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class StreamUseCase(val streamRepository: StreamRepository) {

    fun sendPrompt(
        role: ChatRole,
        content: String,
        imageBytes: ByteArray?
    ): Flow<StreamOllamaEvent> {

        val base64Image = imageBytes?.let { Base64.encode(it) }

        return streamRepository.sendPrompt(
            listOf(
                OllamaMessage(
                    role = role,
                    content = content,
                    images = if (base64Image == null) null else listOf(base64Image)
                )
            )
        )
    }
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
        maxWidth = maxDimension, // Сохраняет пропорции
        maxHeight = maxDimension, // Сохраняет пропорции
        imageFormat = ImageFormat.JPEG // JPEG или PNG
    )

    // 4. Защита на случай, если после перекодирования в JPEG файл внезапно стал больше (редко, но бывает для PNG)
    if (compressedBytes.size < rawBytes.size) {
        compressedBytes
    } else {
        rawBytes
    }
}
