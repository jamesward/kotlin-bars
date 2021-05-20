import java.util.Properties

plugins {
    id("org.jetbrains.compose")
    id("com.android.application")
    kotlin("android")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.3")

    implementation(project(":compose"))

    implementation("androidx.appcompat:appcompat:1.3.0")
    implementation("androidx.activity:activity-compose:1.3.0-alpha08")
}

android {
    compileSdkVersion(30)

    sourceSets["main"].java.srcDirs("src/main/kotlin")

    defaultConfig {
        minSdkVersion(28)

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

    lintOptions {
        disable("ObsoleteLintCustomCheck")
    }

}
