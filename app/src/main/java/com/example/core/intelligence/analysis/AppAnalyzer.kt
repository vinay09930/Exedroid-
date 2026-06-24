package com.example.core.intelligence.analysis

import com.example.domain.models.analysis.AppProfile
import com.example.domain.models.analysis.TargetArchitecture
import com.example.domain.models.analysis.TargetPlatform
import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder

class AppAnalyzer(
    private val dependencyAnalyzer: DependencyAnalyzer = DependencyAnalyzer(),
    private val runtimeAnalyzer: RuntimeAnalyzer = RuntimeAnalyzer(),
    private val frameworkAnalyzer: FrameworkAnalyzer = FrameworkAnalyzer(),
    private val compatibilityAnalyzer: CompatibilityAnalyzer = CompatibilityAnalyzer()
) {

    fun analyze(file: File): AppProfile {
        val platform = detectPlatform(file)
        val architecture = detectArchitecture(file, platform)
        
        val dependencies = dependencyAnalyzer.analyzeDependencies(file)
        val runtimes = runtimeAnalyzer.detectRuntimes(dependencies)
        val frameworks = frameworkAnalyzer.detectFrameworks(dependencies)
        
        val requiresGraphics = frameworkAnalyzer.requiresGraphicsAcceleration(frameworks)
        val memory = compatibilityAnalyzer.estimateMinimumMemory(file.length(), frameworks, runtimes)
        val score = compatibilityAnalyzer.calculateCompatibilityScore(platform, architecture, runtimes, frameworks)

        return AppProfile(
            fileName = file.name,
            fileSize = file.length(),
            platform = platform,
            architecture = architecture,
            dependencies = dependencies,
            requiredRuntimes = runtimes,
            requiredFrameworks = frameworks,
            minimumMemoryMb = memory,
            requiresGraphicsAcceleration = requiresGraphics,
            compatibilityScore = score
        )
    }

    private fun detectPlatform(file: File): TargetPlatform {
        if (!file.exists() || !file.canRead()) return TargetPlatform.UNKNOWN
        val header = ByteArray(4)
        file.inputStream().use { 
            if (it.read(header) < 4) return TargetPlatform.UNKNOWN 
        }

        return when {
            header[0] == 0x4D.toByte() && header[1] == 0x5A.toByte() -> TargetPlatform.WINDOWS // MZ
            header[0] == 0x7F.toByte() && header[1] == 0x45.toByte() && header[2] == 0x4C.toByte() && header[3] == 0x46.toByte() -> TargetPlatform.LINUX // ELF
            isMachO(header) -> TargetPlatform.MACOS
            else -> TargetPlatform.UNKNOWN
        }
    }

    private fun isMachO(header: ByteArray): Boolean {
        val hex = header.joinToString("") { "%02X".format(it) }
        return hex in listOf("FEEDFACE", "CEFAEDFE", "FEEDFACF", "CFFAEDFE", "CAFEBABE", "BEBAFECA")
    }

    private fun detectArchitecture(file: File, platform: TargetPlatform): TargetArchitecture {
        if (!file.exists() || !file.canRead()) return TargetArchitecture.UNKNOWN
        
        return when (platform) {
            TargetPlatform.WINDOWS -> parsePEArchitecture(file)
            TargetPlatform.LINUX -> parseELFArchitecture(file)
            TargetPlatform.MACOS -> parseMachOArchitecture(file)
            TargetPlatform.UNKNOWN -> TargetArchitecture.UNKNOWN
        }
    }

    private fun parsePEArchitecture(file: File): TargetArchitecture {
        val bytes = ByteArray(1024)
        val bytesRead = file.inputStream().use { it.read(bytes) }
        if (bytesRead < 64) return TargetArchitecture.UNKNOWN

        val buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN)
        val peOffset = buffer.getInt(0x3C)
        if (peOffset < 0 || peOffset + 24 > bytesRead) return TargetArchitecture.UNKNOWN

        if (buffer.get(peOffset) != 0x50.toByte() || buffer.get(peOffset + 1) != 0x45.toByte()) {
            return TargetArchitecture.UNKNOWN // PE signature not found
        }

        val machine = buffer.getShort(peOffset + 4).toInt() and 0xFFFF
        return when (machine) {
            0x014C -> TargetArchitecture.X86
            0x8664 -> TargetArchitecture.X86_64
            0x01C0, 0x01C2, 0x01C4 -> TargetArchitecture.ARM
            0xAA64 -> TargetArchitecture.ARM64
            else -> TargetArchitecture.UNKNOWN
        }
    }

    private fun parseELFArchitecture(file: File): TargetArchitecture {
        val bytes = ByteArray(64)
        val bytesRead = file.inputStream().use { it.read(bytes) }
        if (bytesRead < 64) return TargetArchitecture.UNKNOWN

        // ELF e_machine is at offset 0x12
        val buffer = ByteBuffer.wrap(bytes).order(
            if (bytes[5] == 1.toByte()) ByteOrder.LITTLE_ENDIAN else ByteOrder.BIG_ENDIAN
        )
        val machine = buffer.getShort(0x12).toInt() and 0xFFFF
        return when (machine) {
            0x03 -> TargetArchitecture.X86
            0x3E -> TargetArchitecture.X86_64
            0x28 -> TargetArchitecture.ARM
            0xB7 -> TargetArchitecture.ARM64
            else -> TargetArchitecture.UNKNOWN
        }
    }

    private fun parseMachOArchitecture(file: File): TargetArchitecture {
        val bytes = ByteArray(64)
        val bytesRead = file.inputStream().use { it.read(bytes) }
        if (bytesRead < 64) return TargetArchitecture.UNKNOWN
        
        val isLittleEndian = bytes[0] == 0xCE.toByte() || bytes[0] == 0xCF.toByte()
        val buffer = ByteBuffer.wrap(bytes).order(
            if (isLittleEndian) ByteOrder.LITTLE_ENDIAN else ByteOrder.BIG_ENDIAN
        )
        
        val cpuType = buffer.getInt(4)
        
        return when (cpuType) {
            7 -> TargetArchitecture.X86
            16777223 -> TargetArchitecture.X86_64
            12 -> TargetArchitecture.ARM
            16777228 -> TargetArchitecture.ARM64
            else -> TargetArchitecture.UNKNOWN
        }
    }
}
