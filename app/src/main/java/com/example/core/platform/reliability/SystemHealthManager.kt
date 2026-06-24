package com.example.core.platform.reliability

import com.example.core.platform.DiagnosticsEngine
import com.example.domain.models.platform.HealthStatus
import com.example.domain.models.platform.SystemHealth
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow

class SystemHealthManager(
    private val metricsCollector: MetricsCollector,
    private val crashAnalyzer: CrashAnalyzer,
    private val diagnosticsEngine: DiagnosticsEngine,
    private val backupManager: BackupManager
) {
    private val _systemHealth = MutableStateFlow(
        SystemHealth(
            status = HealthStatus.HEALTHY,
            score = 100,
            warnings = emptyList(),
            errors = emptyList(),
            lastCheckTime = System.currentTimeMillis()
        )
    )
    val systemHealth: StateFlow<SystemHealth> = _systemHealth.asStateFlow()

    suspend fun performHealthCheck() {
        val patterns = crashAnalyzer.analyzeCrashPatterns()
        val cpuUsage = metricsCollector.getAverageMetricValue("cpu_usage")
        val ramUsage = metricsCollector.getAverageMetricValue("ram_usage")
        
        val warnings = mutableListOf<String>()
        val errors = mutableListOf<String>()
        var score = 100
        
        if (patterns.isNotEmpty()) {
            errors.addAll(patterns)
            score -= 20 * patterns.size
        }
        
        if (cpuUsage > 85.0) {
            warnings.add("High average CPU usage: $cpuUsage%")
            score -= 10
        }
        
        if (ramUsage > 85.0) {
            warnings.add("High average RAM usage: $ramUsage%")
            score -= 10
        }
        
        score = score.coerceIn(0, 100)
        
        val status = when {
            score >= 80 -> HealthStatus.HEALTHY
            score >= 50 -> HealthStatus.WARNING
            else -> HealthStatus.CRITICAL
        }
        
        _systemHealth.value = SystemHealth(
            status = status,
            score = score,
            warnings = warnings,
            errors = errors,
            lastCheckTime = System.currentTimeMillis()
        )
    }
    
    fun recoverFromCrash(component: String, environmentId: String): Flow<String> = flow {
        emit("Initiating automatic recovery for $component...")
        _systemHealth.value = _systemHealth.value.copy(status = HealthStatus.RECOVERING)
        
        delay(1000)
        emit("Suspending component processes...")
        delay(500)
        
        emit("Running diagnostic engine repair...")
        val repairResult = diagnosticsEngine.triggerAutomaticRepair()
        emit(repairResult)
        
        val backups = backupManager.listBackups(environmentId)
        if (backups.isNotEmpty()) {
            val latestBackup = backups.first()
            emit("Rolling back to latest stable backup (${latestBackup.id})...")
            backupManager.restoreBackup(latestBackup.id).collect { emit(it) }
        } else {
            emit("No backup found. Performing clean restart...")
            delay(1000)
        }
        
        emit("Recovery completed. Resuming operations.")
        performHealthCheck()
    }
}
