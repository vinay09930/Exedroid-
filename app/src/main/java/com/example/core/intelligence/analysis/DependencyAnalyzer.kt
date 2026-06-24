package com.example.core.intelligence.analysis

import java.io.File

class DependencyAnalyzer {

    fun analyzeDependencies(file: File): List<String> {
        val dependencies = mutableListOf<String>()
        if (!file.exists() || !file.canRead()) return dependencies

        val content = extractStrings(file)
        
        val dllRegex = "(?i)[a-z0-9_\\-]+\\.dll".toRegex()
        val soRegex = "(?i)[a-z0-9_\\-]+\\.so(?:\\.[0-9]+)*".toRegex()
        val dylibRegex = "(?i)[a-z0-9_\\-]+\\.dylib".toRegex()
        val frameworkRegex = "(?i)[a-z0-9_\\-]+\\.framework".toRegex()

        dllRegex.findAll(content).forEach { dependencies.add(it.value) }
        soRegex.findAll(content).forEach { dependencies.add(it.value) }
        dylibRegex.findAll(content).forEach { dependencies.add(it.value) }
        frameworkRegex.findAll(content).forEach { dependencies.add(it.value) }

        return dependencies.distinct().sorted()
    }

    private fun extractStrings(file: File): String {
        val limit = 5 * 1024 * 1024L // 5MB limit
        val length = minOf(file.length(), limit).toInt()
        if (length <= 0) return ""
        val bytes = ByteArray(length)
        file.inputStream().use { it.read(bytes) }
        
        val builder = StringBuilder()
        for (b in bytes) {
            val char = b.toInt().toChar()
            if (char in ' '..'~') {
                builder.append(char)
            } else {
                builder.append('\n')
            }
        }
        return builder.toString()
    }
}
