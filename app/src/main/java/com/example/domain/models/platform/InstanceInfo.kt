package com.example.domain.models.platform

data class InstanceInfo(
    val instanceId: String,
    val appId: String,
    val appName: String,
    val status: InstanceStatus,
    val startTime: Long,
    val ramUsageMb: Int,
    val cpuUsagePercent: Float
)

enum class InstanceStatus {
    STARTING,
    RUNNING,
    SUSPENDED,
    STOPPED,
    CRASHED
}
