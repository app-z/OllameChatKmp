package com.kmp.ollamechat.storages.database

import androidx.room.TypeConverter

private const val SEPARATOR = "\u0000"

class Converters {
    @TypeConverter
    fun fromList(list: List<String>?): String? =
        list?.joinToString(SEPARATOR)

    @TypeConverter
    fun toList(data: String?): List<String>? =
        data?.split(SEPARATOR)
}