package com.example.core.managers.runtimes

import com.example.domain.models.RuntimeType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RuntimeInstaller {
    fun installRuntime(type: RuntimeType): Flow<String> = flow {
        emit("Preparing installation for ${type.name}...")
        delay(500)
        when (type) {
            RuntimeType.WINDOWS_WINE -> {
                emit("Setting up Wine integration layer...")
                delay(500)
                emit("Configuring API translation layer...")
                delay(500)
                emit("Initializing Dependency manager...")
            }
            RuntimeType.LINUX_USERSPACE -> {
                emit("Creating Linux userspace environment...")
                delay(500)
                emit("Integrating Package manager...")
                delay(500)
                emit("Setting up Dependency installer...")
            }
            RuntimeType.MACOS_EXPERIMENTAL -> {
                emit("Initializing Experimental compatibility framework...")
                delay(500)
                emit("Configuring Framework detection module...")
                delay(500)
                emit("Setting up Compatibility reporting...")
            }
        }
        delay(500)
        emit("Installation completed for ${type.name}.")
    }
}
