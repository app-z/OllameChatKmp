package com.kmp.ollamechat.generate

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun GenerateScreen(viewModel: GenerateViewModel) {
    val uiState by viewModel.messages.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
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