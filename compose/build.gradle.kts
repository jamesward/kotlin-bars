import org.jetbrains.kotlin.gradle.plugin.extraProperties
import org.jetbrains.kotlin.gradle.utils.loadPropertyFromResources

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("com.android.library")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(11)
    }
}

kotlin {
    android()

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
                implementation(compose.material)
            }
        }
        getByName("iosSimulatorArm64Main") {
            dependsOn(getByName("iosMain"))
        }
    }
}

android {
    namespace = "kotlinbars.compose"
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
