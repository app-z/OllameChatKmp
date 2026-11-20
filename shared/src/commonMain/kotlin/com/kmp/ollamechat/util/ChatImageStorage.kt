package com.kmp.ollamechat.util

import io.github.vinceglb.filekit.FileKit
import io.github.vinceglb.filekit.PlatformFile
import io.github.vinceglb.filekit.createDirectories
import io.github.vinceglb.filekit.delete
import io.github.vinceglb.filekit.div
import io.github.vinceglb.filekit.exists
import io.github.vinceglb.filekit.filesDir
import io.github.vinceglb.filekit.write
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlin.time.Clock
import kotlin.uuid.Uuid

class ChatImageStorage {
    private val imagesDir: PlatformFile
        get() = FileKit.filesDir / "chat_images"

    /**
     * Сохраняет сжатые байты на диск и возвращает ИМЯ файла для записи в БД.
     */
    suspend fun saveImage(bytes: ByteArray): String = withContext(Dispatchers.IO) {
        if (!imagesDir.exists()) {
            imagesDir.createDirectories()
        }

        val uniqueName = "img_${Clock.System.now().toEpochMilliseconds()}_${Uuid.random()}.jpg"
        val targetFile = imagesDir / uniqueName

        targetFile.write(bytes)
        uniqueName
    }

    /**
     * Восстанавливает PlatformFile по имени файла из БД.
     * Возвращает null, если файл был удален.
     */
    fun getImageFile(fileName: String?): PlatformFile? {
        if (fileName.isNullOrBlank()) return null
        val file = imagesDir / fileName
        return if (file.exists()) file else null
    }

    /**
     * Удаляет файл с диска (вызывать при удалении сообщения или очистке чата).
     */
    suspend fun deleteImage(fileName: String?) = withContext(Dispatchers.IO) {
        if (fileName.isNullOrBlank()) return@withContext
        val file = imagesDir / fileName
        if (file.exists()) {
            file.delete()
        }
    }
}
