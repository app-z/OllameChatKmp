package com.kmp.ollamechat.core.network.client.di

import com.kmp.ollamechat.core.datasource.RemoteStreamDataSource
import com.kmp.ollamechat.core.network.client.StreamOllamaClient
import com.kmp.ollamechat.core.network.repository.RemoteStreamDataSourceImpl
import com.kmp.ollamechat.core.network.repository.Settings
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.HttpTimeoutConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule = module {
    single<Settings> { Settings() }

    single<HttpClient> {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    encodeDefaults = true
                }
                )
            }

            install(HttpTimeout) {
                requestTimeoutMillis = HttpTimeoutConfig.INFINITE_TIMEOUT_MS
                socketTimeoutMillis = 120_000
                connectTimeoutMillis = 15_000
            }

        }
    }

    single {
        Json { ignoreUnknownKeys = true; isLenient = true }
    }

    single<Settings> { Settings() }

    single<RemoteStreamDataSource> { RemoteStreamDataSourceImpl(get(), get()) }

    single<StreamOllamaClient> { StreamOllamaClient(get(), get()) }

}
