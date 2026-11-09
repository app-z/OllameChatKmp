package com.kmp.ollamechat.di

import com.kmp.ollamechat.chat.ChatViewModel
import com.kmp.ollamechat.generate.GenerateViewModel
import com.kmp.ollamechat.home.HomeViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::HomeViewModel)
    viewModelOf(::GenerateViewModel)
    viewModelOf(::ChatViewModel)
}
