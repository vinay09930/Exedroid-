package com.example.ui.device

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.domain.models.DeviceInfo
import com.example.domain.repository.DeviceRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DeviceViewModel(
    private val deviceRepository: DeviceRepository
) : ViewModel() {

    val deviceInfo: StateFlow<DeviceInfo?> = deviceRepository.getDeviceInfo()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null
        )

    fun runAnalysis() {
        viewModelScope.launch {
            deviceRepository.analyzeAndSaveDeviceProfile()
        }
    }

    companion object {
        fun provideFactory(deviceRepository: DeviceRepository): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return DeviceViewModel(deviceRepository) as T
            }
        }
    }
}
