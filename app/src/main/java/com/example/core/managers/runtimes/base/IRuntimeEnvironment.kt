package com.example.core.managers.runtimes.base

import com.example.domain.models.RuntimeStatus
import com.example.domain.models.RuntimeType
import kotlinx.coroutines.flow.Flow

interface IRuntimeEnvironment {
    val id: String
    val type: RuntimeType
    val version: String
    val dependencies: List<String>
    
    suspend fun initialize(config: RuntimeConfig): Flow<String>
    suspend fun start(executablePath: String, args: List<String>): Flow<String>
    suspend fun stop(): Boolean
    suspend fun getStatus(): RuntimeStatus
    suspend fun getResourceUsage(): RuntimeResourceUsage
}

data class RuntimeResourceUsage(
    val cpuUsagePercent: Double,
    val memoryUsageMb: Long
)
