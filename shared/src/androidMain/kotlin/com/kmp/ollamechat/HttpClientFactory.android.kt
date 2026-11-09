package com.kmp.ollamechat

import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.okhttp.OkHttp

actual val httpEngine: HttpClientEngineFactory<*> = OkHttp