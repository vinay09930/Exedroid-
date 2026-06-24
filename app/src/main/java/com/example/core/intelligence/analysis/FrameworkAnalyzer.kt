package com.example.core.intelligence.analysis

class FrameworkAnalyzer {

    fun detectFrameworks(dependencies: List<String>): List<String> {
        val frameworks = mutableListOf<String>()
        val lowerDeps = dependencies.map { it.lowercase() }

        // Graphics APIs
        if (lowerDeps.any { it.contains("d3d") || it.contains("dxgi") || it.contains("directx") }) {
            frameworks.add("DirectX")
        }
        if (lowerDeps.any { it.contains("vulkan") || it.contains("vulkan-1") || it.contains("libvulkan") }) {
            frameworks.add("Vulkan")
        }
        if (lowerDeps.any { it.contains("opengl") || it.contains("libgl") }) {
            frameworks.add("OpenGL")
        }
        if (lowerDeps.any { it.contains("metal.framework") }) {
            frameworks.add("Metal")
        }

        // UI Frameworks
        if (lowerDeps.any { it.contains("qt5") || it.contains("qt6") }) {
            frameworks.add("Qt Framework")
        }
        if (lowerDeps.any { it.contains("gtk") || it.contains("libgtk") }) {
            frameworks.add("GTK")
        }
        if (lowerDeps.any { it.contains("user32.dll") || it.contains("gdi32.dll") || it.contains("comctl32.dll") }) {
            frameworks.add("Win32 API")
        }
        if (lowerDeps.any { it.contains("cocoa.framework") || it.contains("appkit.framework") }) {
            frameworks.add("Cocoa / AppKit")
        }
        if (lowerDeps.any { it.contains("flutter") }) {
            frameworks.add("Flutter")
        }
        if (lowerDeps.any { it.contains("unity") }) {
            frameworks.add("Unity Engine")
        }
        if (lowerDeps.any { it.contains("unreal") }) {
            frameworks.add("Unreal Engine")
        }

        return frameworks.distinct().sorted()
    }

    fun requiresGraphicsAcceleration(frameworks: List<String>): Boolean {
        val graphicsApis = setOf("DirectX", "Vulkan", "OpenGL", "Metal", "Unity Engine", "Unreal Engine")
        return frameworks.any { it in graphicsApis }
    }
}
