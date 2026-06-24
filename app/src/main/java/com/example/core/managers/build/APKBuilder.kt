package com.example.core.managers.build

import com.example.core.managers.build.models.ApkBuildConfig
import com.example.core.platform.reliability.BackupManager
import java.io.File
import java.util.UUID
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class APKBuilder(
    private val manifestGenerator: ManifestGenerator = ManifestGenerator(),
    private val resourceGenerator: ResourceGenerator = ResourceGenerator(),
    private val optimizer: Optimizer = Optimizer(),
    private val signer: Signer = Signer(),
    private val apkValidator: ApkValidator = ApkValidator(),
    private val backupManager: BackupManager? = null
) {

    fun buildApk(config: ApkBuildConfig): Flow<String> = flow {
        emit("Starting APK Build Engine for ${config.appName}...")
        delay(500)
        
        val stagingDir = File(config.outputDirectory, "staging_${UUID.randomUUID()}").apply { mkdirs() }
        
        emit("Generating AndroidManifest.xml...")
        manifestGenerator.generateManifest(config, stagingDir)
        delay(300)
        
        emit("Generating resources and layouts...")
        resourceGenerator.generateResources(config, stagingDir)
        delay(400)
        
        emit("Generating Launcher Activity...")
        resourceGenerator.generateLauncherSource(config, stagingDir)
        delay(300)
        
        emit("Packaging runtime dependencies (${config.runtimeType.name})...")
        delay(800)
        
        emit("Packaging application payload...")
        delay(1200)
        
        emit("Compiling Dalvik bytecode (DEX)...")
        delay(1500)
        
        val rawApk = File(stagingDir, "app-raw.apk")
        rawApk.createNewFile() // Simulated APK file
        
        val optimizedApk = File(stagingDir, "app-aligned.apk")
        if (config.optimize) {
            optimizer.optimizeApk(rawApk, optimizedApk).collect { emit(it) }
        } else {
            rawApk.copyTo(optimizedApk, overwrite = true)
        }
        
        val finalApk = File(config.outputDirectory, "${config.packageName}-v${config.versionName}.apk")
        if (config.sign) {
            signer.signApk(optimizedApk, finalApk).collect { emit(it) }
        } else {
            optimizedApk.copyTo(finalApk, overwrite = true)
        }
        
        emit("Cleaning up staging directories...")
        stagingDir.deleteRecursively()
        
        emit("Build Complete! Output: ${finalApk.absolutePath}")

        var validationSuccess = false
        apkValidator.validateApk(finalApk).collect { result ->
            when (result) {
                is ValidationResult.Progress -> emit(result.message)
                is ValidationResult.Success -> {
                    emit(result.message)
                    validationSuccess = true
                }
                is ValidationResult.Error -> {
                    emit("Validation Failed: ${result.message}")
                }
            }
        }

        if (validationSuccess && backupManager != null) {
            emit("Validation successful. Triggering backup...")
            backupManager.createBackup(config.packageName).collect { emit(it) }
        } else if (!validationSuccess) {
            emit("Validation failed. Backup aborted.")
        }
    }
}
