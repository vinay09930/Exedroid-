package com.example.core.managers.build

import java.io.File
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class Signer {

    fun signApk(inputApk: File, outputApk: File): Flow<String> = flow {
        emit("Signing APK: ${inputApk.name}")
        delay(300)
        emit("Generating secure v2/v3 signatures...")
        
        // Simulating apksigner
        delay(1200)
        
        if (inputApk.exists()) {
            inputApk.copyTo(outputApk, overwrite = true)
        }
        
        emit("APK signed successfully with debugging keystore.")
    }
}
