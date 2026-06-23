package com.example.ui.device

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.example.domain.repository.DeviceRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceAnalysisScreen(
    deviceRepository: DeviceRepository,
    onNavigateBack: () -> Unit
) {
    val viewModel: DeviceViewModel = viewModel(factory = DeviceViewModel.provideFactory(deviceRepository))
    val deviceInfo by viewModel.deviceInfo.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Device Intelligence") },
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
        ) {
            if (deviceInfo != null) {
                val info = deviceInfo!!
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("Scores", style = MaterialTheme.typography.titleLarge)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ScoreCard("Performance", info.performanceScore, Modifier.weight(1f))
                        ScoreCard("Compatibility", info.compatibilityScore, Modifier.weight(1f))
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ScoreCard("Development", info.developmentScore, Modifier.weight(1f))
                        ScoreCard("Gaming", info.gamingScore, Modifier.weight(1f))
                    }

                    Text("Hardware", style = MaterialTheme.typography.titleLarge)
                    InfoCard("Model", "${info.manufacturer} ${info.model}")
                    InfoCard("CPU", "${info.cpuArchitecture} (${info.cpuCores} Cores) @ ${info.cpuFrequencies}")
                    InfoCard("GPU", info.gpuInfo)
                    
                    val gb = 1024 * 1024 * 1024
                    InfoCard("RAM", "${info.availableRamBytes / gb} GB Free / ${info.totalRamBytes / gb} GB Total")
                    InfoCard("Storage", "${info.freeStorageBytes / gb} GB Free / ${info.totalStorageBytes / gb} GB Total")

                    Text("System Software", style = MaterialTheme.typography.titleLarge)
                    InfoCard("Android Version", "${info.androidVersion} (API ${info.apiLevel})")
                    InfoCard("Vulkan Support", if (info.vulkanSupport) "Supported" else "Not Supported")
                    InfoCard("OpenGL Support", info.openGlSupport)

                    Text("Health", style = MaterialTheme.typography.titleLarge)
                    InfoCard("Battery", info.batteryHealth)
                    InfoCard("Thermal", info.thermalInfo)
                }
                
                Button(
                    onClick = { viewModel.runAnalysis() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Re-Analyze Device")
                }
            } else {
                Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Button(onClick = { viewModel.runAnalysis() }) {
                        Text("Start Full Analysis")
                    }
                }
            }
        }
    }
}

@Composable
fun ScoreCard(title: String, score: Int, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, style = MaterialTheme.typography.bodyMedium)
            Text(score.toString(), style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
fun InfoCard(title: String, value: String) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(title, style = MaterialTheme.typography.titleSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
