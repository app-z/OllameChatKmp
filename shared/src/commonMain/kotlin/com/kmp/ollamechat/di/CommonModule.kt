package com.kmp.ollamechat.di

import com.kmp.ollamechat.repository.OllamaClient
import com.kmp.ollamechat.repository.Repository
import com.kmp.ollamechat.repository.RepositoryImpl
import com.kmp.ollamechat.repository.Settings
import com.example.chatkmpdontshareui.useCase.OllamaUseCase
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val commonModule = module {
    single { Settings() }

    single {
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                    encodeDefaults = true
                })
            }
        }
    }
    single { OllamaClient(get(), get()) } // settings, httpClient

    single<Repository> { RepositoryImpl(get(), get()) }

    single { OllamaUseCase(get()) }
}
