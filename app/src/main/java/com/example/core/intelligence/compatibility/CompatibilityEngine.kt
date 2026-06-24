package com.example.core.intelligence.compatibility

import com.example.domain.models.DeviceInfo
import com.example.domain.models.analysis.AppProfile
import com.example.domain.models.analysis.CompatibilityReport
import com.example.domain.models.analysis.TargetArchitecture
import com.example.domain.models.analysis.TargetPlatform

class CompatibilityEngine {

    fun evaluateCompatibility(deviceInfo: DeviceInfo, appProfile: AppProfile): CompatibilityReport {
        val issues = mutableListOf<String>()

        val cpuCompat = evaluateCpu(deviceInfo, appProfile, issues)
        val ramCompat = evaluateRam(deviceInfo, appProfile, issues)
        val storageCompat = evaluateStorage(deviceInfo, appProfile, issues)
        val gpuCompat = evaluateGpu(deviceInfo, appProfile, issues)
        val runtimeCompat = evaluateRuntime(appProfile, issues)

        val successProbability = calculateSuccessProbability(cpuCompat, ramCompat, storageCompat, gpuCompat, runtimeCompat, appProfile)
        
        val performanceEstimate = estimatePerformance(successProbability, appProfile)
        val batteryEstimate = estimateBattery(appProfile)
        val thermalEstimate = estimateThermal(appProfile)

        return CompatibilityReport(
            successProbability = successProbability,
            cpuCompatibility = cpuCompat,
            ramCompatibility = ramCompat,
            storageCompatibility = storageCompat,
            gpuCompatibility = gpuCompat,
            runtimeCompatibility = runtimeCompat,
            performanceEstimate = performanceEstimate,
            batteryEstimate = batteryEstimate,
            thermalEstimate = thermalEstimate,
            compatibilityIssues = issues
        )
    }

    private fun evaluateCpu(deviceInfo: DeviceInfo, appProfile: AppProfile, issues: MutableList<String>): Boolean {
        val isDeviceArm64 = deviceInfo.cpuArchitecture.contains("aarch64", ignoreCase = true) || deviceInfo.cpuArchitecture.contains("arm64", ignoreCase = true)
        
        return when (appProfile.architecture) {
            TargetArchitecture.ARM64 -> {
                if (!isDeviceArm64) {
                    issues.add("App requires ARM64, but device is not ARM64.")
                    false
                } else true
            }
            TargetArchitecture.ARM -> true 
            TargetArchitecture.X86_64 -> {
                issues.add("x86_64 translation required. Performance will be degraded.")
                deviceInfo.cpuCores >= 4
            }
            TargetArchitecture.X86 -> {
                issues.add("x86 translation required. Performance will be degraded.")
                true
            }
            TargetArchitecture.UNKNOWN -> {
                issues.add("Unknown architecture. Execution may fail.")
                false
            }
        }
    }

    private fun evaluateRam(deviceInfo: DeviceInfo, appProfile: AppProfile, issues: MutableList<String>): Boolean {
        val availableRamMb = deviceInfo.totalRamBytes / (1024 * 1024)
        if (availableRamMb < appProfile.minimumMemoryMb) {
            issues.add("Insufficient total RAM. Requires ${appProfile.minimumMemoryMb}MB, but device has ${availableRamMb}MB.")
            return false
        }
        
        val freeRamMb = deviceInfo.availableRamBytes / (1024 * 1024)
        if (freeRamMb < appProfile.minimumMemoryMb) {
            issues.add("Warning: Low available RAM right now (${freeRamMb}MB free vs ${appProfile.minimumMemoryMb}MB required).")
        }
        
        return true
    }

    private fun evaluateStorage(deviceInfo: DeviceInfo, appProfile: AppProfile, issues: MutableList<String>): Boolean {
        val requiredStorage = appProfile.fileSize + (50 * 1024 * 1024)
        if (deviceInfo.freeStorageBytes < requiredStorage) {
            issues.add("Insufficient storage space for installation and execution overhead.")
            return false
        }
        return true
    }

    private fun evaluateGpu(deviceInfo: DeviceInfo, appProfile: AppProfile, issues: MutableList<String>): Boolean {
        if (!appProfile.requiresGraphicsAcceleration) return true
        
        if (appProfile.requiredFrameworks.contains("Vulkan") && !deviceInfo.vulkanSupport) {
            issues.add("App requires Vulkan, but device does not support Vulkan.")
            return false
        }
        
        if (appProfile.requiredFrameworks.contains("DirectX") && !deviceInfo.vulkanSupport) {
            issues.add("DirectX to Vulkan translation (DXVK) requires Vulkan support, which is missing.")
            return false
        }

        if (appProfile.requiredFrameworks.contains("OpenGL")) {
            issues.add("OpenGL translation (VirGL/Zink) required. Performance overhead expected.")
        }
        
        return true
    }

    private fun evaluateRuntime(appProfile: AppProfile, issues: MutableList<String>): Boolean {
        if (appProfile.platform == TargetPlatform.UNKNOWN) {
            issues.add("Unknown target platform.")
            return false
        }
        
        if (appProfile.platform == TargetPlatform.MACOS) {
            issues.add("macOS execution (Darling) is highly experimental and may fail.")
        }
        
        if (appProfile.requiredRuntimes.contains(".NET Core / Framework")) {
            issues.add("Mono/Wine-Mono required for .NET execution.")
        }
        
        if (appProfile.requiredRuntimes.contains("Visual C++ Redistributable")) {
            issues.add("Wine/Proton required to provide Visual C++ Runtimes.")
        }

        return true
    }

    private fun calculateSuccessProbability(
        cpu: Boolean, ram: Boolean, storage: Boolean, gpu: Boolean, runtime: Boolean, appProfile: AppProfile
    ): Int {
        if (!ram || !storage) return 0
        
        var baseProb = 100
        
        if (!cpu) baseProb -= 50
        if (!gpu) baseProb -= 40
        if (!runtime) baseProb -= 40
        
        baseProb -= (100 - appProfile.compatibilityScore) / 2
        
        if (appProfile.platform == TargetPlatform.MACOS) {
            baseProb -= 60
        } else if (appProfile.platform == TargetPlatform.WINDOWS) {
            baseProb -= 15
        } else if (appProfile.platform == TargetPlatform.LINUX) {
            baseProb -= 5
        }
        
        if (appProfile.architecture == TargetArchitecture.X86_64) {
            baseProb -= 10
        }
        
        return baseProb.coerceIn(0, 100)
    }

    private fun estimatePerformance(prob: Int, appProfile: AppProfile): String {
        return when {
            prob < 20 -> "Unplayable/Failing"
            prob < 50 -> "Poor (Heavy Stuttering)"
            prob < 80 -> "Fair (Playable with drops)"
            else -> {
                if (appProfile.requiresGraphicsAcceleration || appProfile.architecture == TargetArchitecture.X86_64) "Good"
                else "Excellent"
            }
        }
    }

    private fun estimateBattery(appProfile: AppProfile): String {
        var score = 0
        if (appProfile.requiresGraphicsAcceleration) score += 2
        if (appProfile.architecture == TargetArchitecture.X86_64 || appProfile.architecture == TargetArchitecture.X86) score += 2
        if (appProfile.platform == TargetPlatform.WINDOWS) score += 1

        return when (score) {
            0, 1 -> "Low Drain"
            2, 3 -> "Moderate Drain"
            4 -> "High Drain"
            else -> "Severe Drain"
        }
    }

    private fun estimateThermal(appProfile: AppProfile): String {
        var score = 0
        if (appProfile.requiresGraphicsAcceleration) score += 2
        if (appProfile.architecture == TargetArchitecture.X86_64) score += 2
        if (appProfile.platform == TargetPlatform.WINDOWS) score += 1

        return when (score) {
            0, 1 -> "Cool"
            2, 3 -> "Warm"
            4 -> "Hot"
            else -> "Overheating Risk"
        }
    }
}
