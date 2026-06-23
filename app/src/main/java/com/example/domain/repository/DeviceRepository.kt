package com.example.domain.repository

import com.example.domain.models.DeviceInfo
import kotlinx.coroutines.flow.Flow

interface DeviceRepository {
    fun getDeviceInfo(): Flow<DeviceInfo?>
    suspend fun analyzeAndSaveDeviceProfile()
}
