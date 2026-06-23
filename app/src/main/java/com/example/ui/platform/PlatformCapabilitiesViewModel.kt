package com.example.ui.platform

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.core.platform.PlatformManager
import com.example.domain.models.platform.InstanceInfo
import com.example.domain.models.platform.ResourceStats
import com.example.domain.models.platform.ThermalState
import com.example.domain.models.platform.ThermalStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PlatformCapabilitiesViewModel(private val platformManager: PlatformManager) : ViewModel() {
    
    val instances: StateFlow<List<InstanceInfo>> = platformManager.instanceManager.instances
    
    val resourceStats: StateFlow<ResourceStats?> = platformManager.resourceMonitor.monitorResources()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )
        
    val thermalState: StateFlow<ThermalState?> = platformManager.thermalProtector.monitorThermals()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    private val _messages = MutableStateFlow<List<String>>(emptyList())
    val messages: StateFlow<List<String>> = _messages.asStateFlow()

    fun triggerDiagnostics() {
        viewModelScope.launch {
            platformManager.diagnosticsEngine.runFullDiagnostics().collect { msg ->
                _messages.value = _messages.value + msg
            }
        }
    }

    fun triggerAutoRepair() {
        viewModelScope.launch {
            val msg = platformManager.diagnosticsEngine.triggerAutomaticRepair()
            _messages.value = _messages.value + msg
        }
    }

    fun launchTestInstance() {
        platformManager.instanceManager.launchInstance("test_app", "Test Application")
    }

    fun stopInstance(id: String) {
        platformManager.instanceManager.stopInstance(id)
    }

    companion object {
        fun provideFactory(platformManager: PlatformManager): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return PlatformCapabilitiesViewModel(platformManager) as T
            }
        }
    }
}
