package com.example.ui.ai

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.core.managers.AIManager
import com.example.domain.models.ai.AIProviderMode

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AISystemsScreen(
    aiManager: AIManager,
    onNavigateBack: () -> Unit
) {
    val viewModel: AISystemsViewModel = viewModel(factory = AISystemsViewModel.provideFactory(aiManager))
    val configuration by viewModel.configuration.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI Systems Management") },
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
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // General Enable/Disable
            Card(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("Enable AI Assistant", style = MaterialTheme.typography.titleMedium)
                        Text("Allows AI to manage runtimes, repair crashes, and generate UI.", style = MaterialTheme.typography.bodySmall)
                    }
                    Switch(
                        checked = configuration.isEnabled,
                        onCheckedChange = { viewModel.toggleEnabled(it) }
                    )
                }
            }

            if (configuration.isEnabled) {
                // Provider Mode
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("AI Provider Mode", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Column(Modifier.selectableGroup()) {
                            AIProviderMode.values().forEach { mode ->
                                Row(
                                    Modifier
                                        .fillMaxWidth()
                                        .height(56.dp)
                                        .selectable(
                                            selected = (mode == configuration.mode),
                                            onClick = { viewModel.updateMode(mode) },
                                            role = Role.RadioButton
                                        )
                                        .padding(horizontal = 16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = (mode == configuration.mode),
                                        onClick = null // null recommended for accessibility with screenreaders
                                    )
                                    Text(
                                        text = mode.name,
                                        style = MaterialTheme.typography.bodyLarge,
                                        modifier = Modifier.padding(start = 16.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                // API Key for API_BASED or HYBRID
                if (configuration.mode == AIProviderMode.API_BASED || configuration.mode == AIProviderMode.HYBRID) {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("API Configuration", style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = configuration.apiKey,
                                onValueChange = { viewModel.updateApiKey(it) },
                                label = { Text("API Key") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                        }
                    }
                }

                // Local Model Settings
                if (configuration.mode == AIProviderMode.LOCAL || configuration.mode == AIProviderMode.HYBRID) {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text("Offline Fallback", style = MaterialTheme.typography.titleMedium)
                                Text("Use small local model when offline.", style = MaterialTheme.typography.bodySmall)
                            }
                            Switch(
                                checked = configuration.useLocalModelForOffline,
                                onCheckedChange = { viewModel.toggleOffline(it) }
                            )
                        }
                    }
                }

                // AI Capabilities Overview
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Active AI Engines", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        listOf(
                            "Compatibility AI",
                            "Runtime Selection AI",
                            "Dependency AI",
                            "Repair AI",
                            "Optimization AI",
                            "UI Generation AI"
                        ).forEach { engine ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(engine, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            }
        }
    }
}
