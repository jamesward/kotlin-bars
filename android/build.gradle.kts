import java.util.Properties

plugins {
    id("org.jetbrains.compose")
    id("com.android.application")
    kotlin("android")
}

kotlin {
    jvmToolchain(11)
}

dependencies {
    implementation(project(":compose"))

    implementation("androidx.activity:activity-compose:1.7.2")
}

android {
    namespace = "kotlinbars.android"
    buildToolsVersion = "33.0.2"
    compileSdk = 33

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
