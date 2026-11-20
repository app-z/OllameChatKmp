package com.kmp.ollamechat.di

import com.kmp.ollamechat.chat.MessageUpdateStrategy
import com.kmp.ollamechat.core.usecase.LocalUseCase
import com.kmp.ollamechat.core.usecase.StreamUseCase
import com.kmp.ollamechat.util.ChatImageStorage
import org.koin.dsl.module

val commonModule = module {

    factory<StreamUseCase> { StreamUseCase(get()) }

    factory<LocalUseCase> { LocalUseCase(get()) }

    factory { ChatImageStorage() }

    factory { MessageUpdateStrategy(get(), get()) }


}
