package com.example.core.managers.runtimes

import com.example.domain.models.RuntimeType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RuntimeUpdater {
    fun updateRuntime(type: RuntimeType, currentVersion: String): Flow<String> = flow {
        emit("Checking for updates for ${type.name} (Current: $currentVersion)...")
        delay(500)
        emit("Downloading latest components...")
        delay(500)
        emit("Applying patches...")
        delay(500)
        emit("Update finished successfully.")
    }
}
