package com.example.core.managers.build.models

import com.example.domain.models.AppInfo
import com.example.domain.models.RuntimeType

data class ApkBuildConfig(
    val appInfo: AppInfo,
    val runtimeType: RuntimeType,
    val outputDirectory: String,
    val packageName: String,
    val appName: String,
    val versionName: String,
    val versionCode: Int,
    val optimize: Boolean = true,
    val sign: Boolean = true
)
