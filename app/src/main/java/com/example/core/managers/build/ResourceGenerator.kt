package com.example.core.managers.build

import com.example.core.managers.build.models.ApkBuildConfig
import java.io.File

class ResourceGenerator {

    fun generateResources(config: ApkBuildConfig, stagingDir: File) {
        val resDir = File(stagingDir, "res").apply { mkdirs() }
        
        // Generate strings.xml
        val valuesDir = File(resDir, "values").apply { mkdirs() }
        val stringsContent = """
            <?xml version="1.0" encoding="utf-8"?>
            <resources>
                <string name="app_name">${config.appName}</string>
            </resources>
        """.trimIndent()
        File(valuesDir, "strings.xml").writeText(stringsContent)

        // Generate basic styles
        val stylesContent = """
            <?xml version="1.0" encoding="utf-8"?>
            <resources>
                <style name="Theme.ExeDroidWrapper" parent="android:Theme.Material.Light.NoActionBar" />
                <style name="Theme.ExeDroidWrapper.NoActionBar" parent="Theme.ExeDroidWrapper" />
            </resources>
        """.trimIndent()
        File(valuesDir, "styles.xml").writeText(stylesContent)

        // Setup mipmap directories for launcher icons
        listOf("mdpi", "hdpi", "xhdpi", "xxhdpi", "xxxhdpi").forEach { density ->
            File(resDir, "mipmap-$density").apply { mkdirs() }
        }
        
        // In a complete implementation, this would copy actual icon files.
    }
    
    fun generateLauncherSource(config: ApkBuildConfig, stagingDir: File) {
        val srcDir = File(stagingDir, "src/main/java/${config.packageName.replace('.', '/')}").apply { mkdirs() }
        val launcherContent = """
            package ${config.packageName};

            import android.app.Activity;
            import android.os.Bundle;
            import android.util.Log;

            public class LauncherActivity extends Activity {
                @Override
                protected void onCreate(Bundle savedInstanceState) {
                    super.onCreate(savedInstanceState);
                    Log.i("LauncherActivity", "Starting runtime environment for ${config.appName}");
                    // Here, the generated code would initialize the specific runtime payload 
                    // (e.g., extracting wine/proot and launching the original executable).
                }
            }
        """.trimIndent()
        File(srcDir, "LauncherActivity.java").writeText(launcherContent)
    }
}
