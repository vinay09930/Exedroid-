package com.example.domain.models

enum class RuntimeType {
    WINDOWS_WINE,
    LINUX_USERSPACE,
    MACOS_EXPERIMENTAL
}

enum class RuntimeStatus {
    NOT_INSTALLED,
    INSTALLING,
    INSTALLED,
    UPDATING,
    REPAIRING,
    ERROR
}

data class RuntimeEnvironment(
    val id: String,
    val type: RuntimeType,
    val version: String,
    val status: RuntimeStatus,
    val isDefault: Boolean = false,
    val dependencies: List<String> = emptyList()
)
