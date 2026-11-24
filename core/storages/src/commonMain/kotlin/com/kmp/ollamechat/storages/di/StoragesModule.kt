package com.kmp.ollamechat.storages.di

import com.kmp.ollamechat.domain.datasource.ChatRepository
import com.kmp.ollamechat.domain.datasource.ModelsRepository
import com.kmp.ollamechat.storages.datasource.ChatDataSource
import com.kmp.ollamechat.storages.datasource.ModelsDataSource
import com.kmp.ollamechat.storages.settings.Settings
import org.koin.dsl.module

val storageModule = module {

    single<ChatRepository> { ChatDataSource(get()) }

    single<ModelsRepository> { ModelsDataSource(get()) }

    single <Settings> { Settings() }
}
