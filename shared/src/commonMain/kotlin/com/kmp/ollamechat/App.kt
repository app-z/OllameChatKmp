package com.kmp.ollamechat

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import coil3.ImageLoader
import coil3.compose.setSingletonImageLoaderFactory
import com.kmp.ollamechat.common.di.commonModule
import com.kmp.ollamechat.network.di.networkModule
import com.kmp.ollamechat.storages.di.commonDatabaseModule
import com.kmp.ollamechat.storages.di.storageModule
import com.kmp.ollamechat.di.shouldRefactorModule
import com.kmp.ollamechat.di.viewModelModule
import com.kmp.ollamechat.domain.di.domainModule
import com.kmp.ollamechat.util.platformModule
import io.github.vinceglb.filekit.coil.addPlatformFileSupport
import org.koin.compose.KoinApplication
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.koinConfiguration

@Composable
@Preview
fun App(
    appDeclaration: KoinAppDeclaration = {}
) {

    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .components {
                addPlatformFileSupport()
            }
            .build()
    }

    KoinApplication(configuration = koinConfiguration {
        appDeclaration()
        modules(
            platformModule,
            viewModelModule,
            shouldRefactorModule,
            commonDatabaseModule,
            domainModule,
            networkModule,
            storageModule,
            commonModule
        )
    }) {
        MaterialTheme {
            RootScreen()
        }
    }
}