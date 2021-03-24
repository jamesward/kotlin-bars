plugins {
    id("org.jetbrains.compose")
    id("com.android.application")
    kotlin("android")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.3")

    implementation(project(":common"))

    implementation(compose.runtime)
    implementation(compose.foundation)
    implementation(compose.material)
    implementation(compose.ui)
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.core:core-ktx:1.3.2")
    implementation("androidx.activity:activity-compose:1.3.0-alpha04")

    implementation("com.github.kittinunf.fuel:fuel-coroutines:2.3.1")
    implementation("com.github.kittinunf.fuel:fuel-jackson:2.3.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.9.8")

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
