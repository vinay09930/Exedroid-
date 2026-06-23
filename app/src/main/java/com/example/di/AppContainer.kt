package com.example.di

import android.content.Context
import androidx.room.Room
import com.example.core.intelligence.AppAnalyzerEngine
import com.example.core.intelligence.DeviceIntelligenceEngine
import com.example.core.managers.AIManager
import com.example.core.managers.ApkBuilder
import com.example.core.managers.RuntimeManager
import com.example.core.managers.runtimes.RuntimeInstaller
import com.example.core.managers.runtimes.RuntimeRepairEngine
import com.example.core.managers.runtimes.RuntimeUpdater
import com.example.core.platform.BackupRestoreManager
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
    private val runtimeRepairEngine by lazy { RuntimeRepairEngine() }

    val appRepository: AppRepository by lazy { AppRepositoryImpl(appAnalyzerEngine) }
    
    val deviceRepository: DeviceRepository by lazy { 
        DeviceRepositoryImpl(database.deviceProfileDao(), deviceIntelligenceEngine) 
    }
    
    val runtimeManager: RuntimeManager by lazy { 
        RuntimeManager(runtimeInstaller, runtimeUpdater, runtimeRepairEngine) 
    }
    val aiManager: AIManager by lazy { AIManager() }
    val apkBuilder: ApkBuilder by lazy { ApkBuilder() }
    
    val platformManager: PlatformManager by lazy {
        PlatformManager(
            instanceManager = InstanceManager(),
            resourceMonitor = ResourceMonitor(),
            thermalProtector = ThermalProtector(),
            backupRestoreManager = BackupRestoreManager(),
            diagnosticsEngine = DiagnosticsEngine()
        )
    }
}

