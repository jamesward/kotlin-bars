import java.util.Properties

plugins {
    alias(universe.plugins.jetbrains.compose)
    alias(universe.plugins.android.application)
    //alias(universe.plugins.kotlin.android)
    alias(universe.plugins.kotlin.multiplatform)
}

kotlin {
    jvmToolchain(11)

    androidTarget()

    sourceSets {
        androidMain {
            dependencies {
                implementation(project(":compose"))

                implementation(universe.androidx.activity.compose)
            }
        }
    }
}

android {
    namespace = "kotlinbars.android"
    buildToolsVersion = "34.0.0"
    compileSdk = 34

    defaultConfig {
        minSdk = 28

        val barsUrl: String? by project

        val props = Properties()
        rootProject.file("local.properties").let {
            if (it.exists()) it.inputStream().use(props::load)
        }

        val barsUrlWithFallback = barsUrl ?: props["barsUrl"] as String?

        // 10.0.2.2 is the IP for your machine from the Android emulator
        val barsUrlWithDefault = barsUrlWithFallback ?: "http://10.0.2.2:8080/api/bars"
        resValue("string", "bars_url", barsUrlWithDefault)

        val usesCleartextTraffic = barsUrlWithDefault.startsWith("http://")
        manifestPlaceholders["usesCleartextTraffic"] = usesCleartextTraffic
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

}
