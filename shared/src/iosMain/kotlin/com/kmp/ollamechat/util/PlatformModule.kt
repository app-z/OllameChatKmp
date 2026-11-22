package com.kmp.ollamechat.util

import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    factory<TextSpeaker> { IosTextSpeaker() }
}
