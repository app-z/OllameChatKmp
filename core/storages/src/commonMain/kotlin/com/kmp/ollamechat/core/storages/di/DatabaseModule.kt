package com.kmp.ollamechat.core.storages.di

import com.kmp.ollamechat.core.storages.database.AppDatabase
import com.kmp.ollamechat.core.storages.database.getRoomDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

expect val platformDatabaseModule: Module

val commonDatabaseModule = module {
    includes(platformDatabaseModule)

    single<AppDatabase> {
        getRoomDatabase(builder = get())
    }

    single { get<AppDatabase>().MessagesDao() }
}
