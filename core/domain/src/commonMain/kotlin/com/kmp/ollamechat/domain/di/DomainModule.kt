package com.kmp.ollamechat.domain.di

import com.kmp.ollamechat.domain.usecase.LocalUseCase
import com.kmp.ollamechat.domain.usecase.StreamUseCase
import org.koin.dsl.module

val domainModule = module {

    factory<StreamUseCase> { StreamUseCase(get()) }

    factory<LocalUseCase> { LocalUseCase(get()) }

}
