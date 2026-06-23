package com.example.domain.models.platform

data class ThermalState(
    val temperatureCelsius: Float,
    val status: ThermalStatus
)

enum class ThermalStatus {
    NORMAL,
    WARM,
    HOT,
    CRITICAL
}
