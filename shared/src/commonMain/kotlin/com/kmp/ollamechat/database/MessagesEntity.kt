package com.kmp.ollamechat.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kmp.ollamechat.client.model.ChatRole

@Entity(tableName = "Messages")
data class MessagesEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Long = 0L,
    @ColumnInfo(name = "content") val content: String,
    @ColumnInfo(name = "role") val role: ChatRole,          // "system" | "user" | "assistant" | "tool"
    @ColumnInfo(name = "thinking") val thinking: String? = null,
    @ColumnInfo(name = "date_utc") val dateUtc: String? = null
)
