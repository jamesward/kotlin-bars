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
        namespace = "kotlinbars.rpc"
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
                api(project(":common"))

                implementation("io.ktor:ktor-client-core:2.3.4")
                implementation("io.ktor:ktor-client-content-negotiation:2.3.4")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.4")
            }
        }

        androidMain {
            dependencies {
                implementation("io.ktor:ktor-client-android:2.3.4")
            }
        }

        jvmMain {
            dependencies {
                implementation("io.ktor:ktor-client-java:2.3.4")
            }
        }

        jsMain {
            dependencies {
                api("io.ktor:ktor-client-js:2.3.4")
            }
        }

        iosMain {
            dependencies {
                implementation("io.ktor:ktor-client-ios:2.3.4")
            }
        }

        linuxMain {
            dependencies {
                implementation("io.ktor:ktor-client-curl:2.3.4")
            }
        }
    }
}
