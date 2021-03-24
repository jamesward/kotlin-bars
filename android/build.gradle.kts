plugins {
    id("org.jetbrains.compose")
    id("com.android.application")
    kotlin("android")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.3")

    implementation(project(":compose"))

    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.activity:activity-compose:1.3.0-alpha04")
}

android {
    compileSdkVersion(30)

    sourceSets["main"].java.srcDirs("src/main/kotlin")

    defaultConfig {
        minSdkVersion(28)

        val maybeBarsUrl: String? by project
        // 10.0.2.2 is the IP for your machine from the Android emulator
        val barsUrl = maybeBarsUrl ?: "http://10.0.2.2:8080"
        resValue("string", "bars_url", barsUrl)

        val usesCleartextTraffic = barsUrl.startsWith("http://")
        manifestPlaceholders["usesCleartextTraffic"] = usesCleartextTraffic
    }

    lintOptions {
        disable("ObsoleteLintCustomCheck")
    }

}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}
