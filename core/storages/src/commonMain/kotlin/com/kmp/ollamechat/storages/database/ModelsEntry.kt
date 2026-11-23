package com.kmp.ollamechat.storages.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Models")
data class ModelsEntity(
//    @PrimaryKey(autoGenerate = true)
//    @ColumnInfo("id") val id: Long = 0L,

    @PrimaryKey
    @ColumnInfo("name") val name: String,
    @ColumnInfo("model")  val model: String,
    @ColumnInfo("modified_at") val modifiedAt: String,
    @ColumnInfo("size") val size: Long,
    @ColumnInfo("digest") val digest: String,
    @ColumnInfo("is_selected") val isSelected: Boolean

//    @ColumnInfo(val details: OllamaModelDetails? = null
)