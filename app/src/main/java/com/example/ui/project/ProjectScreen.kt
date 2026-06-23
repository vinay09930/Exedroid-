package com.example.ui.project

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.DesktopWindows
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.DesktopMac
import androidx.compose.material.icons.filled.Archive
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.domain.repository.AppRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProjectScreen(
    appRepository: AppRepository,
    projectId: String,
    onNavigateBack: () -> Unit
) {
    val viewModel: ProjectViewModel = viewModel(factory = ProjectViewModel.provideFactory(appRepository, projectId))
    val appInfo by viewModel.appInfo.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Project Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (appInfo != null) {
            val info = appInfo!!
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // App Icon Placeholder
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .align(Alignment.CenterHorizontally),
                        contentAlignment = Alignment.Center
                    ) {
                        val icon = when (info.type) {
                            "Windows" -> Icons.Default.DesktopWindows
                            "Linux" -> Icons.Default.Computer
                            "macOS" -> Icons.Default.DesktopMac
                            "Archive" -> Icons.Default.Archive
                            else -> Icons.Default.Android
                        }
                        Icon(
                            icon,
                            contentDescription = "App Icon",
                            modifier = Modifier.fillMaxSize(),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    Text(info.name, style = MaterialTheme.typography.headlineMedium, modifier = Modifier.align(Alignment.CenterHorizontally))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        AssistChip(
                            onClick = { },
                            label = { Text(info.status.name) }
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        AssistChip(
                            onClick = { },
                            label = { Text("${info.sizeBytes / (1024 * 1024)} MB") }
                        )
                    }

                    SectionCard(title = "App Information") {
                        InfoRow("Platform", info.type)
                        InfoRow("Architecture", info.architecture)
                    }

                    SectionCard(title = "Detected Requirements") {
                        InfoRow("Dependencies", if (info.detectedDependencies.isEmpty()) "None detected" else info.detectedDependencies.joinToString())
                        InfoRow("Frameworks", if (info.detectedFrameworks.isEmpty()) "None detected" else info.detectedFrameworks.joinToString())
                        InfoRow("Runtimes", if (info.runtimeRequirements.isEmpty()) "None detected" else info.runtimeRequirements.joinToString())
                        InfoRow("Graphics", info.graphicsRequirements)
                        InfoRow("Internet Required", if (info.requiresInternet) "Yes" else "No")
                        InfoRow("Root Required", if (info.requiresRoot) "Yes" else "No")
                        InfoRow("Target Runtimes", if (info.targetRuntimes.isEmpty()) "None" else info.targetRuntimes.joinToString())
                    }

                    SectionCard(title = "Estimations") {
                        InfoRow("RAM Usage", "~${info.estimatedRamUsageMb} MB")
                        InfoRow("Storage Usage", "~${info.estimatedStorageUsageMb} MB")
                        InfoRow("Performance Score", "${info.estimatedPerformanceScore}/100")
                    }

                    SectionCard(title = "Compatibility Report") {
                        Text(info.compatibilityReport, style = MaterialTheme.typography.bodyMedium)
                    }
                    
                    SectionCard(title = "Feasibility Report") {
                        Text(info.feasibilityReport, style = MaterialTheme.typography.bodyMedium)
                    }
                }

                Button(
                    onClick = { /* TODO: Trigger build process */ },
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                ) {
                    Text("Convert to APK")
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun SectionCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
            Spacer(modifier = Modifier.height(8.dp))
            content()
        }
    }
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}
