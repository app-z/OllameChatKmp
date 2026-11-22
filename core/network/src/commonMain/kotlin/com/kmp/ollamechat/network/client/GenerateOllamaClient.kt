package com.kmp.ollamechat.network.client

import com.kmp.ollamechat.common.Settings
import com.kmp.ollamechat.network.datasource.model.GenerateOllamaRequest
import com.kmp.ollamechat.network.datasource.model.GenerateOllamaResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class GenerateOllamaClient(
    private val settings: Settings,
    private val client: HttpClient
) {
    suspend fun sendPrompt(serverUrl: String, prompt: String): Result<String> {
        return try {
            val response: GenerateOllamaResponse = client.post("$serverUrl/api/generate") {
                contentType(ContentType.Application.Json)
                setBody(
                    GenerateOllamaRequest(
                        model = settings.getCurrentModel().id,
                        prompt = prompt,
                        stream = false
                    )
                )

                // header(HttpHeaders.Authorization, "Bearer ВАШ_КЛЮЧ")
            }.body()

            Result.success(response.response ?: "Unknown Error")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
