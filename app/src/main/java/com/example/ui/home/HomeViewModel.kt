package com.example.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.domain.models.AppInfo
import com.example.domain.repository.AppRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val appRepository: AppRepository
) : ViewModel() {

    val recentApps: StateFlow<List<AppInfo>> = appRepository.getRecentApps()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
        
    fun simulateUpload() {
        viewModelScope.launch {
            val randomApps = listOf(
                Pair("Blender.dmg", 1024L * 1024L * 250),
                Pair("Steam.exe", 1024L * 1024L * 2),
                Pair("VSCode.deb", 1024L * 1024L * 90),
                Pair("Game.zip", 1024L * 1024L * 1500)
            )
            val selected = randomApps.random()
            appRepository.processUpload(selected.first, selected.second)
        }
    }

    companion object {
        fun provideFactory(appRepository: AppRepository): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return HomeViewModel(appRepository) as T
            }
        }
    }
}
