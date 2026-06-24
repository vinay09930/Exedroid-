package com.example.core.platform.reliability

import com.example.domain.models.platform.Metric
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MetricsCollector {
    private val _metrics = MutableStateFlow<List<Metric>>(emptyList())
    val metrics: StateFlow<List<Metric>> = _metrics.asStateFlow()

    fun recordMetric(key: String, value: Double, tags: Map<String, String> = emptyMap()) {
        val metric = Metric(
            key = key,
            value = value,
            timestamp = System.currentTimeMillis(),
            tags = tags
        )
        val currentMetrics = _metrics.value.toMutableList()
        currentMetrics.add(metric)
        // Keep only last 1000 metrics for memory safety
        if (currentMetrics.size > 1000) {
            currentMetrics.removeAt(0)
        }
        _metrics.value = currentMetrics
    }

    fun getMetricsByKey(key: String): List<Metric> {
        return _metrics.value.filter { it.key == key }
    }
    
    fun getAverageMetricValue(key: String): Double {
        val filtered = getMetricsByKey(key)
        if (filtered.isEmpty()) return 0.0
        return filtered.map { it.value }.average()
    }
    
    fun clearMetrics() {
        _metrics.value = emptyList()
    }
}
