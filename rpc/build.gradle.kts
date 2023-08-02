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

    ios()
    iosSimulatorArm64()

    linuxX64()

    sourceSets {
        val commonMain = getByName("commonMain") {
            dependencies {
                api(project(":common"))

                implementation("io.ktor:ktor-client-core:2.3.2")
                implementation("io.ktor:ktor-client-content-negotiation:2.3.2")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.2")
            }
        }

        getByName("androidMain") {
            dependencies {
                implementation("io.ktor:ktor-client-android:2.3.2")
            }
        }

        getByName("jvmMain") {
            dependencies {
                implementation("io.ktor:ktor-client-java:2.3.3")
            }
        }

        getByName("jsMain") {
            dependencies {
                api("io.ktor:ktor-client-js:2.3.2")
            }
        }

        val iosMain = getByName("iosMain") {
            dependsOn(commonMain)
            dependencies {
                implementation("io.ktor:ktor-client-ios:2.3.3")
            }
        }

        getByName("iosSimulatorArm64Main") {
            dependsOn(iosMain)
        }

        getByName("linuxX64Main") {
            dependencies {
                implementation("io.ktor:ktor-client-curl:2.3.2")
            }
        }
    }
}

android {
    namespace = "kotlinbars.rpc"
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
