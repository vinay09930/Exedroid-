package com.example.ui.ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.core.managers.AIManager
import com.example.domain.models.ai.AIConfiguration
import com.example.domain.models.ai.AIProviderMode
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AISystemsViewModel(private val aiManager: AIManager) : ViewModel() {
    val configuration: StateFlow<AIConfiguration> = aiManager.configuration

    fun updateMode(mode: AIProviderMode) {
        val current = configuration.value
        aiManager.updateConfiguration(current.copy(mode = mode))
    }

    fun toggleEnabled(enabled: Boolean) {
        val current = configuration.value
        aiManager.updateConfiguration(current.copy(isEnabled = enabled))
    }

    fun updateApiKey(key: String) {
        val current = configuration.value
        aiManager.updateConfiguration(current.copy(apiKey = key))
    }

    fun toggleOffline(offline: Boolean) {
        val current = configuration.value
        aiManager.updateConfiguration(current.copy(useLocalModelForOffline = offline))
    }

    companion object {
        fun provideFactory(aiManager: AIManager): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return AISystemsViewModel(aiManager) as T
            }
        }
    }
}
