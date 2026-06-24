package com.example.domain.models.platform

enum class HealthStatus {
    HEALTHY, WARNING, CRITICAL, RECOVERING, REPAIRING
}

data class SystemHealth(
    val status: HealthStatus,
    val score: Int,
    val warnings: List<String>,
    val errors: List<String>,
    val lastCheckTime: Long
)

data class Metric(
    val key: String,
    val value: Double,
    val timestamp: Long,
    val tags: Map<String, String> = emptyMap()
)

data class CrashReport(
    val id: String,
    val timestamp: Long,
    val component: String,
    val exceptionType: String,
    val stackTrace: String,
    val recoveryActionTaken: String?
)

data class BackupMetadata(
    val id: String,
    val timestamp: Long,
    val sizeBytes: Long,
    val environmentId: String,
    val type: String // "FULL", "INCREMENTAL"
)
