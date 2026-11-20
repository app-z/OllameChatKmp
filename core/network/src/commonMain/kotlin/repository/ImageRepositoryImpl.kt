package com.kmp.ollamechat.core.network.repository

import  com.kmp.ollamechat.core.network.client.model.OllamaChatRequest
import  com.kmp.ollamechat.core.network.client.model.OllamaChatResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class ImageRepositoryImpl(
    private val httpClient: HttpClient,
    private val settings: Settings,
) : ImageRepository {
    override suspend fun sendImagePrompt(
        chatRequests: OllamaChatRequest
    ): OllamaChatResponse {

        val response: OllamaChatResponse = httpClient.post("${settings.getServerUrl()}/api/chat") {
            contentType(ContentType.Application.Json)
            setBody(chatRequests)
        }.body()

        return response
    }
}
