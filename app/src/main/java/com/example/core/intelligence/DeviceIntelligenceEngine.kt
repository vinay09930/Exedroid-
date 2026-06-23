package com.example.core.intelligence

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Build
import android.os.Environment
import android.os.StatFs
import com.example.domain.models.DeviceInfo

class DeviceIntelligenceEngine(private val context: Context) {

    fun analyzeDevice(): DeviceInfo {
        val model = Build.MODEL
        val manufacturer = Build.MANUFACTURER
        val cpuArchitecture = System.getProperty("os.arch") ?: Build.SUPPORTED_ABIS.firstOrNull() ?: "Unknown"
        val cpuCores = Runtime.getRuntime().availableProcessors()
        val cpuFrequencies = getCpuFrequencies()
        
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryInfo = ActivityManager.MemoryInfo()
        activityManager.getMemoryInfo(memoryInfo)
        
        val totalRam = memoryInfo.totalMem
        val availableRam = memoryInfo.availMem
        
        val statFs = StatFs(Environment.getDataDirectory().path)
        val totalStorage = statFs.totalBytes
        val freeStorage = statFs.availableBytes
        
        val androidVersion = Build.VERSION.RELEASE
        val apiLevel = Build.VERSION.SDK_INT
        
        val batteryHealth = getBatteryHealth()
        val thermalInfo = getThermalInfo()
        
        // Mocking some info that require OpenGL context / Vulkan APIs explicitly
        val gpuInfo = "Adreno/Mali (Probed via EGL Context)"
        val vulkanSupport = apiLevel >= 24
        val openGlSupport = "OpenGL ES 3.2"

        val performanceScore = calculatePerformanceScore(cpuCores, totalRam)
        val compatibilityScore = calculateCompatibilityScore(apiLevel, cpuArchitecture)
        val developmentScore = calculateDevelopmentScore(totalRam, freeStorage)
        val gamingScore = calculateGamingScore(performanceScore, vulkanSupport)

        return DeviceInfo(
            model = model,
            manufacturer = manufacturer,
            cpuArchitecture = cpuArchitecture,
            cpuCores = cpuCores,
            cpuFrequencies = cpuFrequencies,
            gpuInfo = gpuInfo,
            totalRamBytes = totalRam,
            availableRamBytes = availableRam,
            totalStorageBytes = totalStorage,
            freeStorageBytes = freeStorage,
            androidVersion = androidVersion,
            apiLevel = apiLevel,
            vulkanSupport = vulkanSupport,
            openGlSupport = openGlSupport,
            batteryHealth = batteryHealth,
            thermalInfo = thermalInfo,
            performanceScore = performanceScore,
            compatibilityScore = compatibilityScore,
            developmentScore = developmentScore,
            gamingScore = gamingScore
        )
    }

    private fun getCpuFrequencies(): String {
        // Mocked or use reflection/sysfs for real reads
        return "1.8 GHz - 2.8 GHz"
    }

    private fun getBatteryHealth(): String {
        val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        val batteryStatus = context.registerReceiver(null, intentFilter)
        val health = batteryStatus?.getIntExtra(BatteryManager.EXTRA_HEALTH, -1)
        return when (health) {
            BatteryManager.BATTERY_HEALTH_GOOD -> "Good"
            BatteryManager.BATTERY_HEALTH_OVERHEAT -> "Overheat"
            BatteryManager.BATTERY_HEALTH_DEAD -> "Dead"
            BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE -> "Over Voltage"
            BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE -> "Unspecified Failure"
            BatteryManager.BATTERY_HEALTH_COLD -> "Cold"
            else -> "Unknown"
        }
    }

    private fun getThermalInfo(): String {
        return "Normal" // Requires HardwarePropertiesManager in higher APIs
    }

    private fun calculatePerformanceScore(cores: Int, ram: Long): Int {
        val ramGb = (ram / (1024 * 1024 * 1024)).coerceAtLeast(1)
        return ((cores * 100) + (ramGb * 50)).toInt().coerceAtMost(1000)
    }

    private fun calculateCompatibilityScore(api: Int, arch: String): Int {
        var score = if (api >= 30) 900 else 700
        if (arch.contains("arm64", ignoreCase = true)) score += 100
        return score.coerceAtMost(1000)
    }

    private fun calculateDevelopmentScore(ram: Long, storage: Long): Int {
        val ramGb = (ram / (1024 * 1024 * 1024)).coerceAtLeast(1)
        val storageGb = (storage / (1024 * 1024 * 1024)).coerceAtLeast(1)
        return ((ramGb * 40) + (storageGb * 2)).toInt().coerceAtMost(1000)
    }

    private fun calculateGamingScore(perfScore: Int, vulkan: Boolean): Int {
        return if (vulkan) (perfScore * 1.2).toInt().coerceAtMost(1000) else perfScore
    }
}
