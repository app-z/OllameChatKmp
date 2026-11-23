package com.kmp.ollamechat.storages.di

import com.kmp.ollamechat.domain.datasource.LocalChatDataSource
import com.kmp.ollamechat.domain.datasource.LocalModelDataSource
import com.kmp.ollamechat.storages.datasource.LocalChatDataSourceImpl
import com.kmp.ollamechat.storages.datasource.LocalModelDataSourceImpl
import com.kmp.ollamechat.storages.settings.Settings
import org.koin.dsl.module

val storageModule = module {

    single<LocalChatDataSource> { LocalChatDataSourceImpl(get()) }

    single<LocalModelDataSource> { LocalModelDataSourceImpl(get()) }

    single <Settings> { Settings() }
}
