package com.kmp.ollamechat

import io.ktor.client.engine.HttpClientEngineFactory

expect val httpEngine: HttpClientEngineFactory<*>