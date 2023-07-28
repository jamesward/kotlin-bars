plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.android.library")
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    jvmToolchain(11)

    androidTarget()

    jvm()

    js(IR) {
        browser()
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    linuxX64()

    sourceSets {
        getByName("commonMain") {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
            }
        }
        getByName("jvmMain") {
            dependencies {
                implementation("org.springframework.data:spring-data-commons:3.1.2")
            }
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.javaParameters = true // this is needed for Spring to reflect parameter names
}

android {
    namespace = "kotlinbars.common"
    @Suppress("UnstableApiUsage")
    buildToolsVersion = "33.0.2"
    compileSdk = 33
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_11)
        targetCompatibility(JavaVersion.VERSION_11)
    }
}
