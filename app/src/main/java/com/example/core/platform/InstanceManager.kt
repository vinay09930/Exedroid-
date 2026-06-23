package com.example.core.platform

import com.example.domain.models.platform.InstanceInfo
import com.example.domain.models.platform.InstanceStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

class InstanceManager {
    private val _instances = MutableStateFlow<List<InstanceInfo>>(emptyList())
    val instances = _instances.asStateFlow()

    fun launchInstance(appId: String, appName: String): String {
        val instanceId = UUID.randomUUID().toString()
        val newInstance = InstanceInfo(
            instanceId = instanceId,
            appId = appId,
            appName = appName,
            status = InstanceStatus.STARTING,
            startTime = System.currentTimeMillis(),
            ramUsageMb = 0,
            cpuUsagePercent = 0f
        )
        _instances.value = _instances.value + newInstance
        // Simulate running state transition
        return instanceId
    }

    fun stopInstance(instanceId: String) {
        _instances.value = _instances.value.map {
            if (it.instanceId == instanceId) it.copy(status = InstanceStatus.STOPPED) else it
        }
    }
}
