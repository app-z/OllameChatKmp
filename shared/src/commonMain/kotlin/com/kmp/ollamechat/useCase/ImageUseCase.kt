package com.kmp.ollamechat.useCase

import com.kmp.ollamechat.client.model.ChatRole
import com.kmp.ollamechat.client.model.OllamaChatRequest
import com.kmp.ollamechat.client.model.OllamaChatResponse
import com.kmp.ollamechat.client.model.OllamaMessage
import com.kmp.ollamechat.repository.ImageRepository
import kotlin.coroutines.cancellation.CancellationException
import kotlin.io.encoding.Base64

class ImageUseCase(
    private val imageRepository: ImageRepository
) {
    suspend fun sendImage(
        prompt: String,
        imageBytes: ByteArray,
        model: String = "gemma4:26b",
    ): Result<OllamaChatResponse> {
        if (imageBytes.isEmpty()) {
            return Result.failure(IllegalArgumentException("Массив байтов изображения пуст"))
        }

        return try {
            val base64Image = Base64.encode(imageBytes)

            val request = OllamaChatRequest(
                model = model,
                messages = listOf(
                    OllamaMessage(
                        role = ChatRole.USER,
                        content = prompt,
                        images = listOf(base64Image)
                    )
                ),
                stream = false
            )

            val response = imageRepository.sendImagePrompt(request)
            Result.success(response)
        } catch (e: CancellationException) {
            // Важно пробрасывать CancellationException дальше, чтобы не сломать отмену корутин
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
