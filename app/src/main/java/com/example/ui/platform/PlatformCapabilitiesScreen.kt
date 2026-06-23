package com.example.ui.platform

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.core.platform.PlatformManager
import com.example.domain.models.platform.InstanceInfo
import com.example.domain.models.platform.ThermalStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlatformCapabilitiesScreen(
    platformManager: PlatformManager,
    onNavigateBack: () -> Unit
) {
    val viewModel: PlatformCapabilitiesViewModel = viewModel(factory = PlatformCapabilitiesViewModel.provideFactory(platformManager))
    val instances by viewModel.instances.collectAsState()
    val resourceStats by viewModel.resourceStats.collectAsState()
    val thermalState by viewModel.thermalState.collectAsState()
    val messages by viewModel.messages.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Platform Capabilities") },
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
            // Enterprise Monitoring
            Text("Real-time Monitoring", style = MaterialTheme.typography.titleLarge)
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Card(modifier = Modifier.weight(1f)) {
                    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("CPU Usage", style = MaterialTheme.typography.titleSmall)
                        Text("${resourceStats?.totalCpuUsagePercent?.toInt() ?: 0}%", style = MaterialTheme.typography.headlineMedium)
                    }
                }
                Card(modifier = Modifier.weight(1f)) {
                    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("RAM Usage", style = MaterialTheme.typography.titleSmall)
                        Text("${resourceStats?.totalRamUsageMb ?: 0} MB", style = MaterialTheme.typography.headlineMedium)
                    }
                }
                Card(
                    modifier = Modifier.weight(1f),
                    colors = CardDefaults.cardColors(
                        containerColor = if (thermalState?.status == ThermalStatus.CRITICAL || thermalState?.status == ThermalStatus.HOT) 
                            MaterialTheme.colorScheme.errorContainer 
                        else MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Thermal", style = MaterialTheme.typography.titleSmall)
                        Text("${thermalState?.temperatureCelsius?.toInt() ?: 0}°C", style = MaterialTheme.typography.headlineMedium)
                    }
                }
            }

            // Management Tools
            Text("Management Tools", style = MaterialTheme.typography.titleLarge)
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(onClick = { viewModel.triggerDiagnostics() }, modifier = Modifier.fillMaxWidth()) {
                        Text("Run Diagnostics")
                    }
                    Button(onClick = { viewModel.triggerAutoRepair() }, modifier = Modifier.fillMaxWidth()) {
                        Text("Trigger Auto Repair")
                    }
                    Button(onClick = { viewModel.launchTestInstance() }, modifier = Modifier.fillMaxWidth()) {
                        Text("Launch Test Instance")
                    }
                }
            }

            // Active Instances
            Text("Active Instances", style = MaterialTheme.typography.titleLarge)
            if (instances.isEmpty()) {
                Text("No instances running.", style = MaterialTheme.typography.bodyMedium)
            } else {
                instances.forEach { instance ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(instance.appName, style = MaterialTheme.typography.titleMedium)
                                AssistChip(onClick = { }, label = { Text(instance.status.name) })
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(onClick = { viewModel.stopInstance(instance.instanceId) }, modifier = Modifier.align(Alignment.End)) {
                                Text("Stop")
                            }
                        }
                    }
                }
            }

            // Logs
            if (messages.isNotEmpty()) {
                Text("Diagnostic Logs", style = MaterialTheme.typography.titleLarge)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        messages.takeLast(5).forEach { msg ->
                            Text(msg, style = MaterialTheme.typography.bodySmall)
                            Spacer(modifier = Modifier.height(4.dp))
                        }
                    }
                }
            }
        }
    }
}
