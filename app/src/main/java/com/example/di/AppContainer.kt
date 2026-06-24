package com.example.di

import android.content.Context
import androidx.room.Room
import com.example.core.intelligence.AppAnalyzerEngine
import com.example.core.intelligence.DeviceIntelligenceEngine
import com.example.core.managers.AIManager
import com.example.core.managers.build.APKBuilder
import com.example.core.managers.runtimes.RuntimeManager
import com.example.core.managers.runtimes.RuntimeInstaller
import com.example.core.managers.runtimes.RuntimeRepairer
import com.example.core.managers.runtimes.RuntimeUpdater
import com.example.core.platform.reliability.BackupManager
import com.example.core.platform.reliability.CrashAnalyzer
import com.example.core.platform.reliability.MetricsCollector
import com.example.core.platform.reliability.SystemHealthManager
import com.example.core.platform.DiagnosticsEngine
import com.example.core.platform.InstanceManager
import com.example.core.platform.PlatformManager
import com.example.core.platform.ResourceMonitor
import com.example.core.platform.ThermalProtector
import com.example.data.local.AppDatabase
import com.example.data.repository.AppRepositoryImpl
import com.example.data.repository.DeviceRepositoryImpl
import com.example.domain.repository.AppRepository
import com.example.domain.repository.DeviceRepository

class AppContainer(private val applicationContext: Context) {
    private val database: AppDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "exedroid_database"
        ).build()
    }

    private val deviceIntelligenceEngine: DeviceIntelligenceEngine by lazy {
        DeviceIntelligenceEngine(applicationContext)
    }
    
    private val appAnalyzerEngine: AppAnalyzerEngine by lazy {
        AppAnalyzerEngine()
    }

    private val runtimeInstaller by lazy { RuntimeInstaller() }
    private val runtimeUpdater by lazy { RuntimeUpdater() }
    private val runtimeRepairer by lazy { RuntimeRepairer() }

    val appRepository: AppRepository by lazy { AppRepositoryImpl(appAnalyzerEngine) }
    
    val deviceRepository: DeviceRepository by lazy { 
        DeviceRepositoryImpl(database.deviceProfileDao(), deviceIntelligenceEngine) 
    }
    
    val runtimeManager: RuntimeManager by lazy { 
        RuntimeManager(runtimeInstaller, runtimeUpdater, runtimeRepairer) 
    }
    val aiManager: AIManager by lazy { AIManager() }
    
    private val diagnosticsEngine by lazy { DiagnosticsEngine() }
    private val backupManager by lazy { BackupManager() }
    private val metricsCollector by lazy { MetricsCollector() }
    private val crashAnalyzer by lazy { CrashAnalyzer() }

    val apkBuilder: APKBuilder by lazy { APKBuilder(backupManager = backupManager) }
    
    val systemHealthManager: SystemHealthManager by lazy {
        SystemHealthManager(
            metricsCollector = metricsCollector,
            crashAnalyzer = crashAnalyzer,
            diagnosticsEngine = diagnosticsEngine,
            backupManager = backupManager
        )
    }

    val platformManager: PlatformManager by lazy {
        PlatformManager(
            instanceManager = InstanceManager(),
            resourceMonitor = ResourceMonitor(),
            thermalProtector = ThermalProtector(),
            diagnosticsEngine = diagnosticsEngine,
            backupManager = backupManager,
            metricsCollector = metricsCollector,
            crashAnalyzer = crashAnalyzer,
            systemHealthManager = systemHealthManager
        )
    }
}

