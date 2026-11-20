package com.kmp.ollamechat.core.storages.di

import com.kmp.ollamechat.core.datasource.LocalChatDataSource
import com.kmp.ollamechat.core.storages.repository.LocalChatDataSourceImpl
import org.koin.dsl.module

val storageModule = module {

    single<LocalChatDataSource> { LocalChatDataSourceImpl(get()) }
}
