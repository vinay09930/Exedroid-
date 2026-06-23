package com.example.core.platform

import com.example.domain.models.platform.ResourceStats
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ResourceMonitor {
    fun monitorResources(): Flow<ResourceStats> = flow {
        while (true) {
            emit(
                ResourceStats(
                    totalCpuUsagePercent = (10..80).random().toFloat(),
                    totalRamUsageMb = (500..4000).random(),
                    freeRamMb = (1000..6000).random(),
                    storageIoReadKbps = (0..5000).random().toFloat(),
                    storageIoWriteKbps = (0..2000).random().toFloat()
                )
            )
            delay(2000)
        }
    }
}
