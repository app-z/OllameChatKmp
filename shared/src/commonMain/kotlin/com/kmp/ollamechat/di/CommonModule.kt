package com.kmp.ollamechat.di

import com.kmp.ollamechat.httpEngine
import com.kmp.ollamechat.useCase.GenerateUseCase
import com.kmp.ollamechat.repository.StreamOllamaClient
import com.kmp.ollamechat.repository.GenerateOllamaClient
import com.kmp.ollamechat.repository.GenerateRepository
import com.kmp.ollamechat.repository.GenerateRepositoryImpl
import com.kmp.ollamechat.repository.Settings
import com.kmp.ollamechat.repository.StreamRepository
import com.kmp.ollamechat.repository.StreamRepositoryImpl
import com.kmp.ollamechat.useCase.StreamUseCase
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.HttpTimeoutConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val commonModule = module {
    single<Settings> { Settings() }

    single<HttpClient> {
        HttpClient(httpEngine) {
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

    single<GenerateOllamaClient> { GenerateOllamaClient(get(), get()) } // settings, httpClient

    single<StreamOllamaClient> { StreamOllamaClient(get(), get(), get()) } // settings, httpClient

    single<GenerateRepository> { GenerateRepositoryImpl(get(), get()) }

    single<StreamRepository> { StreamRepositoryImpl(get(), get()) }

    single<GenerateUseCase> { GenerateUseCase(get()) }

    single<StreamUseCase> { StreamUseCase(get()) }
}
