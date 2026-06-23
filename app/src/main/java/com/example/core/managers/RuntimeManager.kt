package com.example.core.managers

import com.example.core.managers.runtimes.RuntimeInstaller
import com.example.core.managers.runtimes.RuntimeRepairEngine
import com.example.core.managers.runtimes.RuntimeUpdater
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
    private val repairEngine: RuntimeRepairEngine
) {
    private val _installedRuntimes = MutableStateFlow<List<RuntimeEnvironment>>(emptyList())
    val installedRuntimes: StateFlow<List<RuntimeEnvironment>> = _installedRuntimes.asStateFlow()

    init {
        // Mock pre-installed runtimes
        _installedRuntimes.value = listOf(
            RuntimeEnvironment(
                id = UUID.randomUUID().toString(),
                type = RuntimeType.WINDOWS_WINE,
                version = "9.0",
                status = RuntimeStatus.INSTALLED,
                isDefault = true,
                dependencies = listOf("DXVK", "VKD3D", "Mono")
            ),
            RuntimeEnvironment(
                id = UUID.randomUUID().toString(),
                type = RuntimeType.LINUX_USERSPACE,
                version = "Proot-Ubuntu-22.04",
                status = RuntimeStatus.INSTALLED,
                isDefault = false,
                dependencies = listOf("apt", "glibc", "Xwayland")
            )
        )
    }

    fun installRuntime(type: RuntimeType): Flow<String> = flow {
        installer.installRuntime(type).collect { emit(it) }
        val newRuntime = RuntimeEnvironment(
            id = UUID.randomUUID().toString(),
            type = type,
            version = "Latest",
            status = RuntimeStatus.INSTALLED
        )
        _installedRuntimes.value = _installedRuntimes.value + newRuntime
    }

    fun updateRuntime(id: String): Flow<String> = flow {
        val runtime = _installedRuntimes.value.find { it.id == id } ?: return@flow
        updater.updateRuntime(runtime.type, runtime.version).collect { emit(it) }
        _installedRuntimes.value = _installedRuntimes.value.map {
            if (it.id == id) it.copy(version = "Updated-${System.currentTimeMillis()}") else it
        }
    }

    fun repairRuntime(id: String): Flow<String> = flow {
        val runtime = _installedRuntimes.value.find { it.id == id } ?: return@flow
        repairEngine.repairRuntime(runtime.type).collect { emit(it) }
    }
    
    fun getCompatibilityStatus(appName: String, runtimeType: RuntimeType): String {
        return "Compatibility status for $appName on ${runtimeType.name} is currently UNKNOWN."
    }
}
