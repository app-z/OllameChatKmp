package com.kmp.ollamechat.domain.di

import com.kmp.ollamechat.domain.usecase.LocalDbUseCase
import com.kmp.ollamechat.domain.usecase.OllamaModelsUseCase
import com.kmp.ollamechat.domain.usecase.StreamUseCase
import org.koin.dsl.module

val domainModule = module {

    factory<StreamUseCase> { StreamUseCase(get()) }

    factory<LocalDbUseCase> {
        LocalDbUseCase(
            get(),
            get(),
            get()
        )
    }

    factory<OllamaModelsUseCase> { OllamaModelsUseCase(get()) }
}
