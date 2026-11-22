package com.kmp.ollamechat.common.di

import com.kmp.ollamechat.common.AppDispatchers
import com.kmp.ollamechat.common.DefaultAppDispatchers
import com.kmp.ollamechat.common.Settings
import org.koin.dsl.module

var commonModule = module {

    single <Settings> { Settings() }

    single<AppDispatchers> { DefaultAppDispatchers() }
}