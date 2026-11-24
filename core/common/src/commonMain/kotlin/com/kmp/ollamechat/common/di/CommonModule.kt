package com.kmp.ollamechat.common.di

import com.kmp.ollamechat.common.AppDispatchers
import com.kmp.ollamechat.common.DefaultAppDispatchers
import org.koin.dsl.module

var commonModule = module {

    single<AppDispatchers> { DefaultAppDispatchers() }
}
