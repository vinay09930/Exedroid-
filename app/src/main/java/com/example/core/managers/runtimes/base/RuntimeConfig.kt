package com.example.core.managers.runtimes.base

data class RuntimeConfig(
    val environmentPath: String,
    val variables: Map<String, String> = emptyMap(),
    val limits: RuntimeLimits = RuntimeLimits()
)

data class RuntimeLimits(
    val maxCpuCores: Int = -1, // -1 means no limit
    val maxMemoryMb: Long = -1,
    val allowNetworkAccess: Boolean = true,
    val isolatedStoragePath: String? = null
)
