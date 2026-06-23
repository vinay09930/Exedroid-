package com.example.core.intelligence.ai

import com.example.domain.models.AppInfo
import com.example.domain.models.RuntimeType
import kotlinx.coroutines.delay

class RuntimeSelectionAI {
    suspend fun selectBestRuntime(appInfo: AppInfo): RuntimeType {
        delay(800)
        return when {
            appInfo.name.endsWith(".exe") -> RuntimeType.WINDOWS_WINE
            appInfo.name.endsWith(".deb") -> RuntimeType.LINUX_USERSPACE
            appInfo.name.endsWith(".app") -> RuntimeType.MACOS_EXPERIMENTAL
            else -> RuntimeType.LINUX_USERSPACE
        }
    }
}
