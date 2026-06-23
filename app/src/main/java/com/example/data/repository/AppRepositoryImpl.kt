package com.example.data.repository

import com.example.core.intelligence.AppAnalyzerEngine
import com.example.domain.models.AppInfo
import com.example.domain.models.AppStatus
import com.example.domain.repository.AppRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

class AppRepositoryImpl(
    private val appAnalyzerEngine: AppAnalyzerEngine
) : AppRepository {
    private val _appsFlow = MutableStateFlow<List<AppInfo>>(emptyList())
    
    init {
        val initialApps = listOf(
            appAnalyzerEngine.analyze("WinRar.exe", 1024L * 1024L * 5),
            appAnalyzerEngine.analyze("Photoshop.dmg", 1024L * 1024L * 1200),
            appAnalyzerEngine.analyze("VLC.AppImage", 1024L * 1024L * 60)
        )
        _appsFlow.value = initialApps
    }

    override fun getRecentApps(): Flow<List<AppInfo>> = _appsFlow

    override fun getAppById(id: String): Flow<AppInfo?> = _appsFlow.map { apps ->
        apps.find { it.id == id }
    }

    override suspend fun saveApp(appInfo: AppInfo) {
        _appsFlow.value = listOf(appInfo) + _appsFlow.value
    }
    
    override suspend fun processUpload(fileName: String, fileSize: Long) {
        val analyzedApp = appAnalyzerEngine.analyze(fileName, fileSize)
        saveApp(analyzedApp)
    }
}
