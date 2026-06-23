package com.example.domain.models

data class LogEntry(
    val timestamp: Long,
    val tag: String,
    val message: String,
    val level: LogLevel
)

enum class LogLevel {
    INFO, WARNING, ERROR, DEBUG
}
