package com.example.domain.models.ai

enum class AIProviderMode {
    API_BASED,
    LOCAL,
    HYBRID
}

data class AIConfiguration(
    val mode: AIProviderMode = AIProviderMode.HYBRID,
    val isEnabled: Boolean = true,
    val apiKey: String = "",
    val useLocalModelForOffline: Boolean = true
)
