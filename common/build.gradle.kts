plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.android.kotlin.multiplatform.library")
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    jvmToolchain(11)

    androidLibrary {
        @Suppress("UnstableApiUsage")
        namespace = "kotlinbars.common"
        @Suppress("UnstableApiUsage")
        compileSdk = 34
    }

    jvm()

    js {
        browser()
    }

    iosArm64()
    iosSimulatorArm64()

    linuxX64()

    sourceSets {
        commonMain {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
            }
        }
        jvmMain {
            dependencies {
                implementation("org.springframework.data:spring-data-commons:3.1.4")
            }
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.javaParameters = true // this is needed for Spring to reflect parameter names
}
