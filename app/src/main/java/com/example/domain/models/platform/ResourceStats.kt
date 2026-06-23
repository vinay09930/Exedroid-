package com.example.domain.models.platform

data class ResourceStats(
    val totalCpuUsagePercent: Float,
    val totalRamUsageMb: Int,
    val freeRamMb: Int,
    val storageIoReadKbps: Float,
    val storageIoWriteKbps: Float
)
