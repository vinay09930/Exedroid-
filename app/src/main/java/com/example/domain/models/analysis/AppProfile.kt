package com.example.domain.models.analysis

enum class TargetPlatform {
    WINDOWS, LINUX, MACOS, UNKNOWN
}

enum class TargetArchitecture {
    X86, X86_64, ARM, ARM64, UNKNOWN
}

data class AppProfile(
    val fileName: String,
    val fileSize: Long,
    val platform: TargetPlatform,
    val architecture: TargetArchitecture,
    val dependencies: List<String>,
    val requiredRuntimes: List<String>,
    val requiredFrameworks: List<String>,
    val minimumMemoryMb: Long,
    val requiresGraphicsAcceleration: Boolean,
    val compatibilityScore: Int
)
