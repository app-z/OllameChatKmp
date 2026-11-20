package com.kmp.ollamechat.di

import com.kmp.ollamechat.database.AppDatabase
import com.kmp.ollamechat.database.getRoomDatabase
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
