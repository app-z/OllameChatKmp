package com.kmp.ollamechat.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ollamechat.shared.generated.resources.Res
import ollamechat.shared.generated.resources.home
import org.jetbrains.compose.resources.stringResource


@Composable
fun HomeScreen(
    onGenerateRoute: () -> Unit,
    onChatRoute: () -> Unit,
    viewModel: HomeViewModel
) {

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(stringResource(Res.string.home))
            })
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier.padding(paddingValues).padding(12.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Button(onClick = onGenerateRoute) {
                Text(text = "Generate Route")
            }

            Button(onClick = onChatRoute) {
                Text(text = "Chat Route")
            }

        }

    }


}