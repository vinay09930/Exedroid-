package com.example.core.intelligence.analysis

class RuntimeAnalyzer {

    fun detectRuntimes(dependencies: List<String>): List<String> {
        val runtimes = mutableListOf<String>()
        val lowerDeps = dependencies.map { it.lowercase() }

        // Windows MSVC / UCRT
        if (lowerDeps.any { it.startsWith("msvcp") || it.startsWith("vcruntime") }) {
            runtimes.add("Visual C++ Redistributable")
        }
        if (lowerDeps.any { it.startsWith("ucrtbase") }) {
            runtimes.add("Universal C Runtime")
        }

        // .NET
        if (lowerDeps.any { it.contains("mscoree") || it.contains("coreclr") || it.contains("hostfxr") }) {
            runtimes.add(".NET Core / Framework")
        }

        // Java
        if (lowerDeps.any { it.contains("jvm.dll") || it.contains("libjvm.so") || it.contains("libjvm.dylib") }) {
            runtimes.add("Java Runtime Environment (JRE)")
        }

        // Node / Electron
        if (lowerDeps.any { it.contains("node.dll") || it.contains("libnode.so") }) {
            runtimes.add("Node.js / Electron")
        }

        // Python
        if (lowerDeps.any { it.matches("python\\d+\\.dll".toRegex()) || it.matches("libpython\\d+\\.\\d+\\.so".toRegex()) }) {
            runtimes.add("Python Environment")
        }

        // Linux libc
        if (lowerDeps.any { it.startsWith("libc.so") || it.startsWith("ld-linux") }) {
            runtimes.add("glibc")
        }
        
        // macOS objc
        if (lowerDeps.any { it.contains("libobjc") }) {
            runtimes.add("Objective-C Runtime")
        }

        return runtimes.distinct().sorted()
    }
}
