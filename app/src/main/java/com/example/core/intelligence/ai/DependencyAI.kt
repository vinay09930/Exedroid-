package com.example.core.intelligence.ai

import kotlinx.coroutines.delay

class DependencyAI {
    suspend fun analyzeMissingDependencies(logs: String): List<String> {
        delay(1000)
        if (logs.contains("libGL")) return listOf("libgl1-mesa-glx", "libgl1-mesa-dri")
        if (logs.contains("vcredist")) return listOf("vcrun2015", "vcrun2022")
        return listOf("unknown-lib")
    }
}
