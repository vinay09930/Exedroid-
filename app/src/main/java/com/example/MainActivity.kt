package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.device.DeviceAnalysisScreen
import com.example.ui.ai.AISystemsScreen
import com.example.ui.platform.PlatformCapabilitiesScreen
import com.example.ui.home.HomeScreen
import com.example.ui.logs.LogsScreen
import com.example.ui.navigation.Screen
import com.example.ui.project.ProjectScreen
import com.example.ui.runtimes.RuntimesScreen
import com.example.ui.settings.SettingsScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val appContainer = (application as ExeDroidApp).container

        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = Screen.Home.route) {
                        composable(Screen.Home.route) {
                            HomeScreen(
                                appRepository = appContainer.appRepository,
                                onNavigateToProject = { projectId ->
                                    navController.navigate(Screen.Project.createRoute(projectId))
                                },
                                onNavigateToDeviceAnalysis = {
                                    navController.navigate(Screen.DeviceAnalysis.route)
                                },
                                onNavigateToSettings = {
                                    navController.navigate(Screen.Settings.route)
                                },
                                onNavigateToLogs = {
                                    navController.navigate(Screen.Logs.route)
                                },
                                onNavigateToRuntimes = {
                                    navController.navigate(Screen.Runtimes.route)
                                },
                                onNavigateToAISystems = {
                                    navController.navigate(Screen.AISystems.route)
                                },
                                onNavigateToPlatform = {
                                    navController.navigate(Screen.PlatformCapabilities.route)
                                }
                            )
                        }
                        composable(
                            route = Screen.Project.route,
                            arguments = listOf(navArgument("projectId") { type = NavType.StringType })
                        ) { backStackEntry ->
                            val projectId = backStackEntry.arguments?.getString("projectId") ?: return@composable
                            ProjectScreen(
                                appRepository = appContainer.appRepository,
                                projectId = projectId,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                        composable(Screen.DeviceAnalysis.route) {
                            DeviceAnalysisScreen(
                                deviceRepository = appContainer.deviceRepository,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                        composable(Screen.Logs.route) {
                            LogsScreen(
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                        composable(Screen.Settings.route) {
                            SettingsScreen(
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                        composable(Screen.Runtimes.route) {
                            RuntimesScreen(
                                runtimeManager = appContainer.runtimeManager,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                        composable(Screen.AISystems.route) {
                            AISystemsScreen(
                                aiManager = appContainer.aiManager,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                        composable(Screen.PlatformCapabilities.route) {
                            PlatformCapabilitiesScreen(
                                platformManager = appContainer.platformManager,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
