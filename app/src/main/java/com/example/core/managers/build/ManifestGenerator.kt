package com.example.core.managers.build

import com.example.core.managers.build.models.ApkBuildConfig
import java.io.File

class ManifestGenerator {

    fun generateManifest(config: ApkBuildConfig, stagingDir: File): File {
        val manifestContent = """
            <?xml version="1.0" encoding="utf-8"?>
            <manifest xmlns:android="http://schemas.android.com/apk/res/android"
                package="${config.packageName}"
                android:versionCode="${config.versionCode}"
                android:versionName="${config.versionName}">
            
                <uses-permission android:name="android.permission.INTERNET" />
                <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
                <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
                <uses-permission android:name="android.permission.MANAGE_EXTERNAL_STORAGE" />
            
                <application
                    android:allowBackup="true"
                    android:icon="@mipmap/ic_launcher"
                    android:label="@string/app_name"
                    android:roundIcon="@mipmap/ic_launcher_round"
                    android:supportsRtl="true"
                    android:theme="@style/Theme.ExeDroidWrapper"
                    android:requestLegacyExternalStorage="true">
                    
                    <activity
                        android:name=".LauncherActivity"
                        android:exported="true"
                        android:label="@string/app_name"
                        android:theme="@style/Theme.ExeDroidWrapper.NoActionBar"
                        android:configChanges="orientation|keyboardHidden|screenSize">
                        <intent-filter>
                            <action android:name="android.intent.action.MAIN" />
                            <category android:name="android.intent.category.LAUNCHER" />
                        </intent-filter>
                    </activity>
                </application>
            </manifest>
        """.trimIndent()

        val manifestFile = File(stagingDir, "AndroidManifest.xml")
        manifestFile.writeText(manifestContent)
        return manifestFile
    }
}
