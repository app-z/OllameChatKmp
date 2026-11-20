package com.kmp.ollamechat.core.storages.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kmp.ollamechat.core.network.client.model.ChatRole
///import com.kmp.ollamechat.client.model.ChatRole
import kotlin.time.Clock

@Entity(tableName = "Messages")
data class MessagesEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long = 0L,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "role") val role: ChatRole,          // "system" | "user" | "assistant" | "tool"
    @ColumnInfo(name = "thinking") val thinking: String? = null,
    @ColumnInfo(name = "timestamp") val timestamp: Long = Clock.System.now().toEpochMilliseconds(),
    @ColumnInfo(name = "image_path") val imagePath: String? = null
)
