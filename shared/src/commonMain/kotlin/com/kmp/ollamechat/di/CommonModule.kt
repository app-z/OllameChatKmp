package com.kmp.ollamechat.di

import com.kmp.ollamechat.chat.MessageUpdateStrategy
import com.kmp.ollamechat.util.ChatImageStorage
import org.koin.dsl.module

val shouldRefactorModule = module {


    factory { ChatImageStorage() }

    factory { MessageUpdateStrategy(get(), get()) }
}
