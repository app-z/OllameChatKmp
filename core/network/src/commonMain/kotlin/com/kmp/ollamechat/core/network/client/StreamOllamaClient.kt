package com.kmp.ollamechat.core.network.client

import com.kmp.ollamechat.core.domain.model.OllamaMessage
import com.kmp.ollamechat.core.domain.model.StreamOllamaEvent
import com.kmp.ollamechat.core.network.client.mapper.toChatMessageDtoList
import com.kmp.ollamechat.core.network.client.model.OllamaChatChunk
import com.kmp.ollamechat.core.network.client.model.OllamaChatRequest
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
 * Возвращает Flow<OllamaEvent> по мере генерации + Done/Error.
 */
class StreamOllamaClient(
    private val client: HttpClient,
    private val json: Json
) {
    fun streamChat(
        serverUrl: String,
        ollamaMessages: List<OllamaMessage>,
        modelId: String,
    ): Flow<StreamOllamaEvent> = flow {
        var finalEvent: StreamOllamaEvent? = null

        try {
            client.preparePost("$serverUrl/api/chat") {
                contentType(ContentType.Application.Json)
                setBody(
                    OllamaChatRequest(
                        model = modelId,
                        messages = ollamaMessages.toChatMessageDtoList(),
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
                        break
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
