package com.example.domain.models

data class AppInfo(
    val id: String,
    val name: String,
    val type: String, // "Windows", "Linux", "macOS", "Archive"
    val architecture: String, // "x86", "x64", "ARM", "Universal"
    val sizeBytes: Long,
    val uploadDate: Long,
    val status: AppStatus,
    
    // Metadata
    val extractedIconPath: String? = null,
    val detectedDependencies: List<String> = emptyList(),
    val detectedFrameworks: List<String> = emptyList(),
    val runtimeRequirements: List<String> = emptyList(),
    val graphicsRequirements: String = "Unknown",
    val requiresInternet: Boolean = false,
    val requiresRoot: Boolean = false,
    
    // Reports
    val compatibilityReport: String = "",
    val feasibilityReport: String = "",
    val estimatedRamUsageMb: Int = 0,
    val estimatedStorageUsageMb: Int = 0,
    val estimatedPerformanceScore: Int = 0,
    
    // Runtimes
    val targetRuntimes: List<String> = emptyList() // List of RuntimeType names
)

enum class AppStatus {
    UPLOADED, ANALYZING, READY, CONVERTING, COMPLETED, FAILED
}
