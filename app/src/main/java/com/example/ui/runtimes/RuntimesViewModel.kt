package com.example.ui.runtimes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.core.managers.RuntimeManager
import com.example.domain.models.RuntimeEnvironment
import com.example.domain.models.RuntimeType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RuntimesViewModel(private val runtimeManager: RuntimeManager) : ViewModel() {
    val runtimes = runtimeManager.installedRuntimes

    private val _messages = MutableStateFlow<List<String>>(emptyList())
    val messages: StateFlow<List<String>> = _messages.asStateFlow()

    fun installRuntime(type: RuntimeType) {
        viewModelScope.launch {
            runtimeManager.installRuntime(type).collect { msg ->
                _messages.value = _messages.value + msg
            }
        }
    }

    fun updateRuntime(id: String) {
        viewModelScope.launch {
            runtimeManager.updateRuntime(id).collect { msg ->
                _messages.value = _messages.value + msg
            }
        }
    }

    fun repairRuntime(id: String) {
        viewModelScope.launch {
            runtimeManager.repairRuntime(id).collect { msg ->
                _messages.value = _messages.value + msg
            }
        }
    }

    companion object {
        fun provideFactory(runtimeManager: RuntimeManager): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return RuntimesViewModel(runtimeManager) as T
            }
        }
    }
}
