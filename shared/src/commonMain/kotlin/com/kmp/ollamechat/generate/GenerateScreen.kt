package com.kmp.ollamechat.generate

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ollamechat.shared.generated.resources.Res
import ollamechat.shared.generated.resources.generate
import org.jetbrains.compose.resources.stringResource

@Composable
fun GenerateScreen(
    viewModel: GenerateViewModel,
    onBack: () -> Unit
) {

    val uiState by viewModel.messagesUiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.generate)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )

                    }
                }
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(paddingValues)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            LaunchedEffect(Unit) {
                viewModel.sendMessage("привет!")
            }

            when (uiState) {
                is GenerateUIState.GenerateMessage -> {
                    Text(text = (uiState as GenerateUIState.GenerateMessage).message)
                }

                is GenerateUIState.Error -> {
                    Text(text = (uiState as GenerateUIState.Error).error)
                }

                GenerateUIState.Idle -> {

                }
            }
        }
    }
}