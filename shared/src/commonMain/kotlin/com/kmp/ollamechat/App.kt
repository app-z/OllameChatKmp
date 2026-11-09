package com.kmp.ollamechat

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.kmp.ollamechat.di.commonModule
import com.kmp.ollamechat.di.viewModelModule
import com.kmp.ollamechat.home.HomeViewModel
import org.koin.compose.KoinApplication
import org.koin.compose.viewmodel.koinViewModel
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.koinConfiguration

@Composable
@Preview
fun App(
    appDeclaration: KoinAppDeclaration = {}
) {
    KoinApplication(configuration = koinConfiguration {
        modules(
            viewModelModule,
            commonModule
        )
    }) {
        val homeViewModel = koinViewModel<HomeViewModel>()

        MaterialTheme {
            RootScreen()
        }
    }
}