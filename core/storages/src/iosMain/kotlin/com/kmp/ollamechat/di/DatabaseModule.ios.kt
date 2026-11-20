package com.kmp.ollamechat.core.storages.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.kmp.ollamechat.core.storages.database.AppDatabase
import com.kmp.ollamechat.core.storages.database.DB_Name
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.module.Module
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

// iosMain/.../DatabaseModule.ios.kt
@OptIn(ExperimentalForeignApi::class)
actual val platformDatabaseModule: Module = module {
    single<RoomDatabase.Builder<AppDatabase>> {
        val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null
        )
        val dbFilePath = requireNotNull(documentDirectory?.path) + "/${DB_Name}"
        Room.databaseBuilder<AppDatabase>(
            name = dbFilePath
        )
    }
}
