package com.example.core.platform.reliability

import com.example.domain.models.platform.CrashReport
import java.util.UUID
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class CrashAnalyzer {
    private val _crashReports = MutableStateFlow<List<CrashReport>>(emptyList())
    val crashReports: StateFlow<List<CrashReport>> = _crashReports.asStateFlow()

    fun logCrash(
        component: String,
        exception: Throwable,
        recoveryAction: String? = null
    ): CrashReport {
        val report = CrashReport(
            id = UUID.randomUUID().toString(),
            timestamp = System.currentTimeMillis(),
            component = component,
            exceptionType = exception.javaClass.simpleName,
            stackTrace = exception.stackTraceToString(),
            recoveryActionTaken = recoveryAction
        )
        
        _crashReports.value = _crashReports.value + report
        return report
    }

    fun analyzeCrashPatterns(): List<String> {
        val patterns = mutableListOf<String>()
        val recentCrashes = _crashReports.value.takeLast(10)
        
        val componentsCrashing = recentCrashes.groupingBy { it.component }.eachCount()
        componentsCrashing.forEach { (component, count) ->
            if (count >= 3) {
                patterns.add("Component '$component' is unstable (crashed $count times recently).")
            }
        }
        
        return patterns
    }
    
    fun getReportsForComponent(component: String): List<CrashReport> {
        return _crashReports.value.filter { it.component == component }
    }
}
