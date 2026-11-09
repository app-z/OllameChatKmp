package com.kmp.ollamechat.client

import com.kmp.ollamechat.chat.ChatRequest
import com.kmp.ollamechat.chat.OllamaChatChunk
import com.kmp.ollamechat.chat.OllamaChatRequest
import com.kmp.ollamechat.repository.Settings
import com.kmp.ollamechat.repository.model.StreamOllamaEvent
import io.ktor.client.HttpClient
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.post
import io.ktor.client.request.preparePost
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsChannel
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.utils.io.readUTF8Line
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
        modelId: String = settings.getCurrentModel().id
    ): Flow<StreamOllamaEvent> = flow {
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

                // NDJSON: читаем построчно, пока поток не закончится
                while (!channel.isClosedForRead) {
                    val line = channel.readUTF8Line() ?: break
                    if (line.isBlank()) continue

                    val chunk = try {
                        json.decodeFromString<OllamaChatChunk>(line)
                    } catch (e: Exception) {
                        emit(StreamOllamaEvent.Error("Не удалось распарсить чанк: $line", e))
                        break
                    }
//                    println("RAW: $line")

                    chunk.error?.let {
                        emit(StreamOllamaEvent.Error(it))
                        break
                    }

                    val msg = chunk.message
                    msg?.thinking?.takeIf { it.isNotEmpty() }
                        ?.let {
                            emit(StreamOllamaEvent.Thinking(it))
                        }

                    msg?.content?.takeIf { it.isNotEmpty() }
                        ?.let {
                            emit(StreamOllamaEvent.Token(it))
                        }

                    if (chunk.done) {
                        emit(StreamOllamaEvent.Done(chunk.evalCount))
                        break
                    }
                }

                // Поток закрылся без done=true — считаем завершённым
                emit(StreamOllamaEvent.Done(null))
            }
        } catch (e: ResponseException) {
            emit(StreamOllamaEvent.Error("HTTP ${e.response.status.value}", e))
        } catch (e: Exception) {
            emit(StreamOllamaEvent.Error(e.message ?: "Unknown error", e))
        }
    }
}
