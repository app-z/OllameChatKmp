package com.kmp.ollamechat.common.di

import com.kmp.ollamechat.common.AiModelsSettings
import com.kmp.ollamechat.common.AppDispatchers
import com.kmp.ollamechat.common.DefaultAppDispatchers
import org.koin.dsl.module

var commonModule = module {

    single<AiModelsSettings> { AiModelsSettings() }

    single<AppDispatchers> { DefaultAppDispatchers() }
}
