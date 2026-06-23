package com.example.ui.logs

import androidx.lifecycle.ViewModel
import com.example.domain.models.LogEntry
import com.example.domain.models.LogLevel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LogsViewModel : ViewModel() {
    private val _logs = MutableStateFlow<List<LogEntry>>(listOf(
        LogEntry(System.currentTimeMillis(), "System", "ExeDroid initialized", LogLevel.INFO),
        LogEntry(System.currentTimeMillis(), "Runtime", "Checking environment dependencies...", LogLevel.DEBUG)
    ))
    val logs: StateFlow<List<LogEntry>> = _logs.asStateFlow()
}
