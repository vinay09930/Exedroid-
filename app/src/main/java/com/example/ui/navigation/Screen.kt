package com.example.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Project : Screen("project/{projectId}") {
        fun createRoute(projectId: String) = "project/$projectId"
    }
    object DeviceAnalysis : Screen("device_analysis")
    object Logs : Screen("logs")
    object Settings : Screen("settings")
    object Runtimes : Screen("runtimes")
    object AISystems : Screen("ai_systems")
    object PlatformCapabilities : Screen("platform_capabilities")
}
