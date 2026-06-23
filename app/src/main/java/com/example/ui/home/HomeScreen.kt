package com.example.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.domain.models.AppInfo
import com.example.domain.repository.AppRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    appRepository: AppRepository,
    onNavigateToProject: (String) -> Unit,
    onNavigateToDeviceAnalysis: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToLogs: () -> Unit,
    onNavigateToRuntimes: () -> Unit,
    onNavigateToAISystems: () -> Unit,
    onNavigateToPlatform: () -> Unit
) {
    val viewModel: HomeViewModel = viewModel(factory = HomeViewModel.provideFactory(appRepository))
    val recentApps by viewModel.recentApps.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ExeDroid") },
                actions = {
                    IconButton(onClick = onNavigateToLogs) {
                        Icon(Icons.Default.Warning, contentDescription = "Logs")
                    }
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { viewModel.simulateUpload() },
                icon = { Icon(Icons.Default.Add, contentDescription = "Upload App") },
                text = { Text("Upload App") }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                DeviceStatusCard(onClick = onNavigateToDeviceAnalysis)
            }
            
            item {
                RuntimeManagementCard(onClick = onNavigateToRuntimes)
            }
            
            item {
                AISystemsCard(onClick = onNavigateToAISystems)
            }

            item {
                PlatformCapabilitiesCard(onClick = onNavigateToPlatform)
            }
            
            item {
                CompatibilityDatabaseCard()
            }

            item {
                Text(
                    text = "Recent Projects",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            items(recentApps) { app ->
                AppItemCard(appInfo = app, onClick = { onNavigateToProject(app.id) })
            }
        }
    }
}

@Composable
fun DeviceStatusCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Analytics, contentDescription = null, modifier = Modifier.size(40.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text("Device Analysis", style = MaterialTheme.typography.titleMedium)
                Text("Tap to view compatibility score", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun RuntimeManagementCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Settings, contentDescription = null, modifier = Modifier.size(40.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text("Runtime Environments", style = MaterialTheme.typography.titleMedium)
                Text("Manage isolated translation layers", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun AISystemsCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Settings, contentDescription = null, modifier = Modifier.size(40.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text("AI Systems", style = MaterialTheme.typography.titleMedium)
                Text("Manage intelligent translation & UI", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun PlatformCapabilitiesCard(onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Analytics, contentDescription = null, modifier = Modifier.size(40.dp))
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text("Platform Capabilities", style = MaterialTheme.typography.titleMedium)
                Text("Advanced tools, monitoring & management", style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun CompatibilityDatabaseCard() {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Compatibility Database", style = MaterialTheme.typography.titleMedium)
            Text("Search known compatible Windows/Linux apps.", style = MaterialTheme.typography.bodyMedium)
            // Placeholder for search bar
        }
    }
}

@Composable
fun AppItemCard(appInfo: AppInfo, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = onClick
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(appInfo.name, style = MaterialTheme.typography.titleMedium)
            Text("Platform: ${appInfo.type} | Arch: ${appInfo.architecture}", style = MaterialTheme.typography.bodyMedium)
            Text("Status: ${appInfo.status.name}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
        }
    }
}
