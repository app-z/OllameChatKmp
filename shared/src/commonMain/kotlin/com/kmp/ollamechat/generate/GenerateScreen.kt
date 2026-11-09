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
import com.example.chatkmpdontshareui.viewModel.ChatUIState

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
            is ChatUIState.ChatMessage -> {
                Text(text = (uiState as ChatUIState.ChatMessage).message)
            }

            is ChatUIState.Error -> {
                Text(text = (uiState as ChatUIState.Error).error)
            }

            ChatUIState.Idle -> {

            }
        }
    }
}