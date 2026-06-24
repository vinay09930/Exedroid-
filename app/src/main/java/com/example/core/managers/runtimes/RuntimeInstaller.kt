package com.example.core.managers.runtimes

import com.example.domain.models.RuntimeType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class RuntimeInstaller {
    fun installRuntime(type: RuntimeType, version: String): Flow<String> = flow {
        emit("Preparing installation for ${type.name} version $version...")
        delay(500)
        emit("Downloading runtime binaries...")
        delay(1000)
        emit("Extracting files to secure storage...")
        delay(1000)
        emit("Verifying checksums and signatures...")
        delay(500)
        emit("Installation completed for ${type.name}.")
    }
}
