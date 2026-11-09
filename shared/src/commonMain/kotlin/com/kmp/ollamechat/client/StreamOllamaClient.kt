package com.kmp.ollamechat.client

import com.kmp.ollamechat.chat.ChatRequest
import com.kmp.ollamechat.chat.OllamaChatChunk
import com.kmp.ollamechat.chat.OllamaChatRequest
import com.kmp.ollamechat.repository.Settings
import com.kmp.ollamechat.repository.model.StreamOllamaEvent
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.preparePost
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.utils.io.readLine
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json

/**
 * streamChat()
 * Стриминговый запрос к /api/chat.
 * Возвращает Flow<OllamaEvent>: токены по мере генерации + Done/Error.
 * Flow отменяемый — отмена корутины закрывает соединение и останавливает генерацию.
 */
class StreamOllamaClient(
    private val settings: Settings,
    private val client: HttpClient,
    private val json: Json
) {
    fun streamChat(
        serverUrl: String,
        messages: List<ChatRequest>,
        modelId: String,
    ): Flow<StreamOllamaEvent> = flow {
        // Переменная для отслеживания финального статуса: SUCCESS, ERROR или PREMATURE_CLOSE
        var finalEvent: StreamOllamaEvent? = null

        try {
            client.preparePost("$serverUrl/api/chat") {
                contentType(ContentType.Application.Json)
                setBody(
                    OllamaChatRequest(
                        model = modelId,
                        messages = messages,
                        keepAlive = "10m",
                        stream = true
                    )
                )
            }.execute { response ->
                val channel = response.bodyAsChannel()

                while (!channel.isClosedForRead) {
                    val line = channel.readLine() ?: break
                    if (line.isBlank()) continue

                    val chunk = try {
                        json.decodeFromString<OllamaChatChunk>(line)
                    } catch (e: Exception) {
                        finalEvent = StreamOllamaEvent.Error("Не удалось распарсить чанк: $line", e)
                        break // Просто выходим из цикла, выполнение пойдет ниже
                    }

                    if (chunk.error != null) {
                        finalEvent = StreamOllamaEvent.Error(chunk.error)
                        break
                    }

                    val msg = chunk.message
                    msg?.thinking?.takeIf { it.isNotEmpty() }?.let {
                        emit(StreamOllamaEvent.Thinking(it))
                    }

                    msg?.content?.takeIf { it.isNotEmpty() }?.let {
                        emit(StreamOllamaEvent.Token(it))
                    }

                    if (chunk.done) {
                        finalEvent = StreamOllamaEvent.Done(chunk.evalCount)
                        break
                    }
                }

                // Если цикл прервался, но finalEvent не выставлен (например, сервер закрыл канал раньше времени)
                val eventToSend = finalEvent ?: StreamOllamaEvent.Done(null)
                emit(eventToSend)
            }
        } catch (e: ResponseException) {
            emit(StreamOllamaEvent.Error("HTTP ${e.response.status.value}", e))
        } catch (e: Exception) {
            emit(StreamOllamaEvent.Error(e.message ?: "Unknown error", e))
        }
    }
}
