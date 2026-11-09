package com.kmp.ollamechat.repository

import com.example.chatkmpdontshareui.repository.model.OllamaRequest
import com.example.chatkmpdontshareui.repository.model.OllamaResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class OllamaClient(
    private val settings: Settings,
    private val client: HttpClient
) {
    suspend fun sendPrompt(serverUrl: String, prompt: String): Result<String> {
        return try {
            val response: OllamaResponse = client.post("$serverUrl/api/generate") {
                contentType(ContentType.Application.Json)
                // Вставляем объект запроса. Ktor сам его сериализует в JSON.
                setBody(
                    OllamaRequest(
                        model = settings.getCurrentModel().id,
                        prompt = prompt,
                        stream = false
                    )
                )

                // Если используете Ollama Cloud, раскомментируйте:
                // header(HttpHeaders.Authorization, "Bearer ВАШ_КЛЮЧ")
            }.body() // Получаем и десериализуем ответ

            Result.success(response.response ?: "Unknown Error")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
