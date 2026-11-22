package com.kmp.ollamechat.storages.di

import com.kmp.ollamechat.domain.datasource.LocalChatDataSource
import com.kmp.ollamechat.storages.datasource.LocalChatDataSourceImpl
import org.koin.dsl.module

val storageModule = module {

    single<LocalChatDataSource> { LocalChatDataSourceImpl(get(), get()) }
}
