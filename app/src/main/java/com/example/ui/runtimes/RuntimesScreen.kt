package com.example.ui.runtimes

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.core.managers.RuntimeManager
import com.example.domain.models.RuntimeEnvironment
import com.example.domain.models.RuntimeType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RuntimesScreen(
    runtimeManager: RuntimeManager,
    onNavigateBack: () -> Unit
) {
    val viewModel: RuntimesViewModel = viewModel(factory = RuntimesViewModel.provideFactory(runtimeManager))
    val runtimes by viewModel.runtimes.collectAsState()
    val messages by viewModel.messages.collectAsState()
    
    var showInstallDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Runtime Environments") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showInstallDialog = true },
                icon = { Icon(Icons.Default.Build, contentDescription = "Install Runtime") },
                text = { Text("Install Runtime") }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (messages.isNotEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Activity Log", style = MaterialTheme.typography.titleSmall)
                        messages.takeLast(3).forEach { msg ->
                            Text(msg, style = MaterialTheme.typography.bodySmall)
                        }
                    }
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(runtimes) { runtime ->
                    RuntimeCard(
                        runtime = runtime,
                        onUpdate = { viewModel.updateRuntime(runtime.id) },
                        onRepair = { viewModel.repairRuntime(runtime.id) }
                    )
                }
            }
        }
    }

    if (showInstallDialog) {
        AlertDialog(
            onDismissRequest = { showInstallDialog = false },
            title = { Text("Install New Runtime") },
            text = {
                Column {
                    RuntimeType.values().forEach { type ->
                        TextButton(
                            onClick = {
                                viewModel.installRuntime(type)
                                showInstallDialog = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(type.name)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showInstallDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}

@Composable
fun RuntimeCard(
    runtime: RuntimeEnvironment,
    onUpdate: () -> Unit,
    onRepair: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(runtime.type.name, style = MaterialTheme.typography.titleMedium)
                AssistChip(onClick = { }, label = { Text(runtime.status.name) })
            }
            Text("Version: ${runtime.version}", style = MaterialTheme.typography.bodyMedium)
            if (runtime.isDefault) {
                Text("Default Environment", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Dependencies: ${if(runtime.dependencies.isEmpty()) "None" else runtime.dependencies.joinToString()}", style = MaterialTheme.typography.bodySmall)
            
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(onClick = onRepair) {
                    Text("Repair")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = onUpdate) {
                    Text("Update")
                }
            }
        }
    }
}
