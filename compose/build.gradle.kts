plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("com.android.library")
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    targetHierarchy.default()

    jvmToolchain(11)

    androidTarget()

    jvm()

    ios {
        binaries.framework {
            isStatic = true
            baseName = "KotlinbarsCompose"
        }
    }

    iosSimulatorArm64 {
        binaries.framework {
            isStatic = true
            baseName = "KotlinbarsCompose"
        }
    }

    sourceSets {
        getByName("commonMain") {
            dependencies {
                api(project(":rpc"))

                implementation(compose.ui)
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
            }
        }
    }
}

android {
    namespace = "kotlinbars.compose"
    @Suppress("UnstableApiUsage")
    buildToolsVersion = "33.0.2"
    compileSdk = 33

    defaultConfig {
        minSdk = 28
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}
