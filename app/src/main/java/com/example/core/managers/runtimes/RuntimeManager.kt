package com.example.core.managers.runtimes

import com.example.core.managers.runtimes.base.IRuntimeEnvironment
import com.example.core.managers.runtimes.base.RuntimeConfig
import com.example.core.managers.runtimes.base.RuntimeLimits
import com.example.core.managers.runtimes.linux.LinuxRuntimeEnvironment
import com.example.core.managers.runtimes.macos.MacOsRuntimeEnvironment
import com.example.core.managers.runtimes.windows.WindowsRuntimeEnvironment
import com.example.domain.models.RuntimeEnvironment
import com.example.domain.models.RuntimeStatus
import com.example.domain.models.RuntimeType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import java.util.UUID

class RuntimeManager(
    private val installer: RuntimeInstaller,
    private val updater: RuntimeUpdater,
    private val repairer: RuntimeRepairer
) {
    private val _installedRuntimes = MutableStateFlow<List<RuntimeEnvironment>>(emptyList())
    val installedRuntimes: StateFlow<List<RuntimeEnvironment>> = _installedRuntimes.asStateFlow()
    
    private val activeEnvironments = mutableMapOf<String, IRuntimeEnvironment>()

    fun installRuntime(type: RuntimeType, version: String = "Latest"): Flow<String> = flow {
        installer.installRuntime(type, version).collect { emit(it) }
        val newRuntime = RuntimeEnvironment(
            id = UUID.randomUUID().toString(),
            type = type,
            version = version,
            status = RuntimeStatus.INSTALLED,
            dependencies = getDependenciesForType(type)
        )
        _installedRuntimes.value = _installedRuntimes.value + newRuntime
    }

    fun updateRuntime(id: String, targetVersion: String = "Latest"): Flow<String> = flow {
        val runtime = _installedRuntimes.value.find { it.id == id } ?: return@flow
        updater.updateRuntime(runtime.type, runtime.version, targetVersion).collect { emit(it) }
        _installedRuntimes.value = _installedRuntimes.value.map {
            if (it.id == id) it.copy(version = targetVersion) else it
        }
    }

    fun repairRuntime(id: String): Flow<String> = flow {
        val runtime = _installedRuntimes.value.find { it.id == id } ?: return@flow
        repairer.repairRuntime(runtime.type).collect { emit(it) }
    }
    
    suspend fun getOrCreateEnvironment(id: String): IRuntimeEnvironment? {
        val runtime = _installedRuntimes.value.find { it.id == id } ?: return null
        
        if (activeEnvironments.containsKey(id)) {
            return activeEnvironments[id]
        }
        
        val env = when (runtime.type) {
            RuntimeType.WINDOWS_WINE -> WindowsRuntimeEnvironment(id, runtime.version, runtime.dependencies)
            RuntimeType.LINUX_USERSPACE -> LinuxRuntimeEnvironment(id, runtime.version, runtime.dependencies)
            RuntimeType.MACOS_EXPERIMENTAL -> MacOsRuntimeEnvironment(id, runtime.version, runtime.dependencies)
        }
        
        activeEnvironments[id] = env
        return env
    }
    
    suspend fun launchApplication(runtimeId: String, executablePath: String, args: List<String>): Flow<String> = flow {
        val env = getOrCreateEnvironment(runtimeId)
        if (env == null) {
            emit("Error: Runtime not found.")
            return@flow
        }
        
        val config = RuntimeConfig(
            environmentPath = "/data/local/tmp/exedroid/runtimes/$runtimeId",
            limits = RuntimeLimits(maxCpuCores = 4, maxMemoryMb = 2048)
        )
        
        env.initialize(config).collect { emit(it) }
        env.start(executablePath, args).collect { emit(it) }
    }

    suspend fun stopApplication(runtimeId: String) {
        activeEnvironments[runtimeId]?.stop()
    }

    fun getCompatibilityStatus(appName: String, runtimeType: RuntimeType): String {
        return "Compatibility status for $appName on ${runtimeType.name} is currently UNKNOWN."
    }

    private fun getDependenciesForType(type: RuntimeType): List<String> {
        return when (type) {
            RuntimeType.WINDOWS_WINE -> listOf("DXVK", "VKD3D", "Wine")
            RuntimeType.LINUX_USERSPACE -> listOf("PRoot", "glibc", "Xwayland")
            RuntimeType.MACOS_EXPERIMENTAL -> listOf("Darling", "Cocoa-Stub")
        }
    }
}
