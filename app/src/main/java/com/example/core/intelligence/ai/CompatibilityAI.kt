package com.example.core.intelligence.ai

import com.example.domain.models.AppInfo
import kotlinx.coroutines.delay

class CompatibilityAI {
    suspend fun predictSuccessRate(appInfo: AppInfo): Int {
        delay(1000)
        return (50..99).random() // Mock success rate percentage
    }

    suspend fun predictPerformance(appInfo: AppInfo): String {
        delay(800)
        return "Expected performance: High. Minimal frame drops expected."
    }
}
