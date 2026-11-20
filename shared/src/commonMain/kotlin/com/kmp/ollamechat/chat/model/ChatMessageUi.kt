package com.kmp.ollamechat.chat.model

//import com.kmp.ollamechat.client.model.ChatRole
import com.kmp.ollamechat.core.network.client.model.ChatRole
import io.github.vinceglb.filekit.PlatformFile

data class ChatMessageUi(
    val id: Long,
    val role: ChatRole,
    val content: String,
    val thinking: String?,
    val imageFile: PlatformFile?
)
