package com.example.domain.repository

import com.example.domain.models.AppInfo
import kotlinx.coroutines.flow.Flow

interface AppRepository {
    fun getRecentApps(): Flow<List<AppInfo>>
    fun getAppById(id: String): Flow<AppInfo?>
    suspend fun saveApp(appInfo: AppInfo)
    suspend fun processUpload(fileName: String, fileSize: Long)
}
