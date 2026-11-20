package com.kmp.ollamechat.core.usecase

import com.kmp.ollamechat.core.datasource.RemoteStreamDataSource
import com.kmp.ollamechat.core.domain.model.ChatRole
import com.kmp.ollamechat.core.domain.model.OllamaMessage
import com.kmp.ollamechat.core.domain.model.StreamOllamaEvent
import kotlinx.coroutines.flow.Flow
import kotlin.io.encoding.Base64

class StreamUseCase(val remoteStreamDataSource: RemoteStreamDataSource) {

    fun sendPrompt(
        role: ChatRole,
        content: String,
        imageBytes: ByteArray?
    ): Flow<StreamOllamaEvent> {

        val base64Image = imageBytes?.let { Base64.encode(it) }

        return remoteStreamDataSource.sendPrompt(
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


