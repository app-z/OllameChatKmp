package com.kmp.ollamechat.di

import com.kmp.ollamechat.chatscreen.ChatViewModel
import com.kmp.ollamechat.homescreen.HomeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::ChatViewModel)
}
