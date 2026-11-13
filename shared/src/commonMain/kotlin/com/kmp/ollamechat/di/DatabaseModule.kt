package com.kmp.ollamechat.di

import com.kmp.ollamechat.database.AppDatabase
import com.kmp.ollamechat.database.getRoomDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

// commonMain/.../DatabaseModule.kt
expect val platformDatabaseModule: Module

val commonDatabaseModule = module {
    includes(platformDatabaseModule)

    // Собираем саму базу данных, получая builder из платформенного модуля
    single<AppDatabase> {
        getRoomDatabase(builder = get())
    }

    // Регистрируем DAO (пример)
    single { get<AppDatabase>().MessagesDao() }
}
