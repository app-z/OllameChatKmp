package com.kmp.ollamechat.core.storages.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.kmp.ollamechat.core.storages.database.AppDatabase
import com.kmp.ollamechat.core.storages.database.DB_Name
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformDatabaseModule: Module = module {
    single<RoomDatabase.Builder<AppDatabase>> {
        val appContext = androidContext().applicationContext
        val dbFile = appContext.getDatabasePath(DB_Name)
        Room.databaseBuilder<AppDatabase>(
            context = appContext,
            name = dbFile.absolutePath
        )
    }
}
