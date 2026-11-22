package com.kmp.ollamechat.util

import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    factory<TextSpeaker> { AndroidTextSpeaker(androidContext()) }
}
