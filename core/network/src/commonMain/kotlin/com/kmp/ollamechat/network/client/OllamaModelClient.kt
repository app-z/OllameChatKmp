package com.kmp.ollamechat.network.client

import com.kmp.ollamechat.network.client.model.OllamaModelDto
import com.kmp.ollamechat.network.client.model.OllamaTagsResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import kotlinx.io.IOException

class OllamaModelClient(
    private val httpClient: HttpClient
) {
    suspend fun getModelsTag(serverUrl: String) : Result<List<OllamaModelDto>> {
        val response : HttpResponse = httpClient.get("${serverUrl}/api/tags") {
            contentType(ContentType.Application.Json)
        }
        return try {
            if (response.status.isSuccess()) {
                Result.success(response.body<OllamaTagsResponseDto>().models)
            } else {
                Result.failure(IOException("Ollama returned ${response.status}"))
            }
        } catch (e: Exception) {
            Result.failure(e) //Net error, and other
        }
    }
}
