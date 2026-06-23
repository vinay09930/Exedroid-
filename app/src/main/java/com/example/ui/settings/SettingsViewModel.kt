package com.example.ui.settings

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class SettingsState(
    val runtimeEnabled: Boolean = true,
    val aiAnalysisEnabled: Boolean = true,
    val performanceMode: Boolean = false,
    val developerMode: Boolean = false
)

class SettingsViewModel : ViewModel() {
    private val _settingsState = MutableStateFlow(SettingsState())
    val settingsState: StateFlow<SettingsState> = _settingsState.asStateFlow()

    fun toggleRuntime(enabled: Boolean) {
        _settingsState.value = _settingsState.value.copy(runtimeEnabled = enabled)
    }

    fun toggleAiAnalysis(enabled: Boolean) {
        _settingsState.value = _settingsState.value.copy(aiAnalysisEnabled = enabled)
    }

    fun togglePerformanceMode(enabled: Boolean) {
        _settingsState.value = _settingsState.value.copy(performanceMode = enabled)
    }

    fun toggleDeveloperMode(enabled: Boolean) {
        _settingsState.value = _settingsState.value.copy(developerMode = enabled)
    }
}
