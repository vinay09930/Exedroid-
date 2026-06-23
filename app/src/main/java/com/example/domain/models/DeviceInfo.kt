package com.example.domain.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "device_profiles")
data class DeviceInfo(
    @PrimaryKey val id: String = "local_device",
    val model: String,
    val manufacturer: String,
    val cpuArchitecture: String,
    val cpuCores: Int,
    val cpuFrequencies: String,
    val gpuInfo: String,
    val totalRamBytes: Long,
    val availableRamBytes: Long,
    val totalStorageBytes: Long,
    val freeStorageBytes: Long,
    val androidVersion: String,
    val apiLevel: Int,
    val vulkanSupport: Boolean,
    val openGlSupport: String,
    val batteryHealth: String,
    val thermalInfo: String,
    
    // Generated Scores
    val performanceScore: Int,
    val compatibilityScore: Int,
    val developmentScore: Int,
    val gamingScore: Int,
    
    val timestamp: Long = System.currentTimeMillis()
)
