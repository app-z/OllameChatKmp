package com.kmp.ollamechat.di

import com.kmp.ollamechat.chat.ChatViewModel
import com.kmp.ollamechat.home.HomeViewModel
import com.kmp.ollamechat.settings.SettingsViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::SettingsViewModel)
    viewModelOf(::ChatViewModel)
    viewModelOf(::HomeViewModel)
}
