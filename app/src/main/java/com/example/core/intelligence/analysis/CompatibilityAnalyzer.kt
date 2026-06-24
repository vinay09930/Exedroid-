package com.example.core.intelligence.analysis

import com.example.domain.models.analysis.TargetArchitecture
import com.example.domain.models.analysis.TargetPlatform

class CompatibilityAnalyzer {

    fun calculateCompatibilityScore(
        platform: TargetPlatform,
        architecture: TargetArchitecture,
        runtimes: List<String>,
        frameworks: List<String>
    ): Int {
        var score = 100

        // Architecture mapping penalty
        if (architecture == TargetArchitecture.X86_64) {
            score -= 10 // Translation layer overhead (e.g., Box86/Box64)
        } else if (architecture == TargetArchitecture.X86) {
            score -= 15
        } else if (architecture == TargetArchitecture.UNKNOWN) {
            score -= 50
        }

        // Platform complexity penalty
        if (platform == TargetPlatform.WINDOWS) {
            score -= 5 // Wine/Proton overhead
        } else if (platform == TargetPlatform.MACOS) {
            score -= 20 // Darling/macOS emulation overhead
        } else if (platform == TargetPlatform.UNKNOWN) {
            score -= 50
        }

        // Framework and graphics penalties
        if (frameworks.contains("DirectX")) {
            score -= 5 // DXVK translation overhead
        }
        if (frameworks.contains("Unreal Engine") || frameworks.contains("Unity Engine")) {
            score -= 10 // Heavy rendering requirements
        }
        if (runtimes.contains("Node.js / Electron")) {
            score -= 10 // V8 memory/CPU overhead
        }

        // Return bounded score
        return score.coerceIn(0, 100)
    }

    fun estimateMinimumMemory(fileSize: Long, frameworks: List<String>, runtimes: List<String>): Long {
        var baseMemoryMb = 256L

        if (fileSize > 50 * 1024 * 1024) {
            baseMemoryMb += 256L
        }

        if (frameworks.contains("Unreal Engine") || frameworks.contains("Unity Engine")) {
            baseMemoryMb += 2048L
        } else if (frameworks.contains("DirectX") || frameworks.contains("Vulkan")) {
            baseMemoryMb += 1024L
        }

        if (frameworks.contains("Qt Framework") || frameworks.contains("GTK")) {
            baseMemoryMb += 512L
        }
        
        if (runtimes.contains("Node.js / Electron") || runtimes.contains("Java Runtime Environment (JRE)")) {
            baseMemoryMb += 512L
        }

        return baseMemoryMb
    }
}
