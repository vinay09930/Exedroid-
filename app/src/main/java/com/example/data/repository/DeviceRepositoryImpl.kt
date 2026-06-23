package com.example.data.repository

import com.example.core.intelligence.DeviceIntelligenceEngine
import com.example.data.local.DeviceProfileDao
import com.example.domain.models.DeviceInfo
import com.example.domain.repository.DeviceRepository
import kotlinx.coroutines.flow.Flow

class DeviceRepositoryImpl(
    private val deviceProfileDao: DeviceProfileDao,
    private val engine: DeviceIntelligenceEngine
) : DeviceRepository {

    override fun getDeviceInfo(): Flow<DeviceInfo?> {
        return deviceProfileDao.getLatestDeviceProfile()
    }

    override suspend fun analyzeAndSaveDeviceProfile() {
        val deviceInfo = engine.analyzeDevice()
        deviceProfileDao.insertProfile(deviceInfo)
    }
}
