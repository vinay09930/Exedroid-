package com.example.domain.models.analysis

data class CompatibilityReport(
    val successProbability: Int,
    val cpuCompatibility: Boolean,
    val ramCompatibility: Boolean,
    val storageCompatibility: Boolean,
    val gpuCompatibility: Boolean,
    val runtimeCompatibility: Boolean,
    val performanceEstimate: String,
    val batteryEstimate: String,
    val thermalEstimate: String,
    val compatibilityIssues: List<String>
)
