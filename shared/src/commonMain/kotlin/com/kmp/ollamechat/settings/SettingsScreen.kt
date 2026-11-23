package com.kmp.ollamechat.settings

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.kmp.ollamechat.domain.model.OllamaModel
import ollamechat.shared.generated.resources.Res
import ollamechat.shared.generated.resources.settings
import ollamechat.shared.generated.resources.unknown_error
import ollamechat.shared.generated.resources.update_models
import org.jetbrains.compose.resources.stringResource

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onBack: () -> Unit
) {

    val state by viewModel.ollamaModelsTagFlow.collectAsStateWithLifecycle()

    val listState = rememberLazyListState()

    val snackbarHostState = remember { SnackbarHostState() }

    val unknownError = stringResource(Res.string.unknown_error)

    LaunchedEffect(state) {
        val errorState = state as? UiState.Error ?: return@LaunchedEffect
        snackbarHostState.showSnackbar(
            message = errorState.ex.message ?: unknownError,
            duration = SnackbarDuration.Long
        )
    }

    LaunchedEffect(state) {
        if (state is UiState.Loading) {
            listState.scrollToItem(0)
        }
    }
 
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.settings)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        viewModel.requestOllamaModels()

                    }) {
                    Text(text = stringResource(Res.string.update_models))
                }
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {

            when (state) {
                UiState.Loading -> {
                    ProgressIndicator()
                }

                is UiState.Error -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = (state as UiState.Error).ex.message ?: unknownError,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(Modifier.padding(8.dp))
                        Button(onClick = { viewModel.requestOllamaModels() }) {
                            Text(stringResource(Res.string.update_models))
                        }
                    }
                }

                is UiState.Success -> {
                    ModelListScreen(
                        (state as UiState.Success).ollamaModel,
                        listState,
                        onModelClick = { model ->
                            viewModel.onModelUpdateClick(model)
                        })
                }
            }
        }

    }
}

@Composable
fun ModelListScreen(
    itemsModels: List<OllamaModel>,
    listState: LazyListState,
    onModelClick: (model: OllamaModel) -> Unit
) {

    LazyColumn(
        modifier = Modifier.padding(12.dp),
        state = listState
    ) {
        items(
            items = itemsModels,
            key = { model -> model.name }
        ) { model ->
            ModelItem(
                model = model,
                modifier = Modifier.animateItem(
                    fadeInSpec = tween(300),
                    placementSpec = spring(stiffness = Spring.StiffnessMediumLow)
                ),
                onModelClick = { onModelClick(model) }
            )

        }
    }
}


@Composable
fun ModelItem(
    modifier: Modifier,
    model: OllamaModel,
    onModelClick: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onModelClick.invoke() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = model.name,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )

        Spacer(Modifier.width(12.dp))

        Checkbox(
            checked = model.isSelected,
            onCheckedChange = null,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun ProgressIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}