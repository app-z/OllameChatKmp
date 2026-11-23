package com.kmp.ollamechat.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kmp.ollamechat.domain.model.OllamaModel
import com.kmp.ollamechat.domain.usecase.LocalDbUseCase
import com.kmp.ollamechat.domain.usecase.OllamaModelsUseCase
import com.kmp.ollamechat.util.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val ollamaModelsUseCase: OllamaModelsUseCase,
    private val localDbUseCase: LocalDbUseCase
) : ViewModel() {

    private val _ollamaModelsTagFlow = MutableStateFlow<UiState>(UiState.Loading)

    val ollamaModelsTagFlow = _ollamaModelsTagFlow.asStateFlow()

    init {

        viewModelScope.launch {

            if (localDbUseCase.getModelList().isEmpty()) {
                requestOllamaModels()
            }

            localDbUseCase.observeModelList().collect { ollamaModels ->
                _ollamaModelsTagFlow.emit(UiState.Success(ollamaModels))

                if (ollamaModels.isNotEmpty() && !ollamaModels.any { it.isSelected }) {
                    localDbUseCase.selectModel(ollamaModels.first().name)
                }
            }
        }
    }

    fun requestOllamaModels() {
        viewModelScope.launch {
            _ollamaModelsTagFlow.emit(UiState.Loading)

            val ollamaModelsTag = ollamaModelsUseCase.getOllamaModels()
            ollamaModelsTag.onSuccess { models ->
                localDbUseCase.updateModelList(models)
            }

            ollamaModelsTag.onFailure { throwable ->
                _ollamaModelsTagFlow.emit(UiState.Error(throwable))
            }
        }
    }

    fun onModelUpdateClick(model: OllamaModel) {
        Logger.d(model.model)
        viewModelScope.launch {
            localDbUseCase.selectModel(model.model)
        }
    }
}
