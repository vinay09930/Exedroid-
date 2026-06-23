package com.example.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit
) {
    val viewModel: SettingsViewModel = viewModel()
    val state by viewModel.settingsState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            SettingsSwitchItem(
                title = "Runtime Emulation",
                description = "Enable Proot/Wine for desktop apps",
                checked = state.runtimeEnabled,
                onCheckedChange = { viewModel.toggleRuntime(it) }
            )
            
            SettingsSwitchItem(
                title = "AI Analysis",
                description = "Use AI to analyze compatibility",
                checked = state.aiAnalysisEnabled,
                onCheckedChange = { viewModel.toggleAiAnalysis(it) }
            )

            SettingsSwitchItem(
                title = "Performance Mode",
                description = "Maximize CPU/RAM for conversion",
                checked = state.performanceMode,
                onCheckedChange = { viewModel.togglePerformanceMode(it) }
            )

            SettingsSwitchItem(
                title = "Developer Mode",
                description = "Show advanced logs and tools",
                checked = state.developerMode,
                onCheckedChange = { viewModel.toggleDeveloperMode(it) }
            )
        }
    }
}

@Composable
fun SettingsSwitchItem(
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.titleMedium)
                Text(description, style = MaterialTheme.typography.bodyMedium)
            }
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
    }
}
