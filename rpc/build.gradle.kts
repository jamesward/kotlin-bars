plugins {
    alias(universeunstable.plugins.kotlin.multiplatform)
    alias(universeunstable.plugins.kotlin.plugin.serialization)
    alias(universeunstable.plugins.android.kotlin.multiplatform.library)
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


                implementation(universe.ktor.client.core)
                implementation(universe.ktor.client.content.negotiation)
                implementation(universe.ktor.serialization.kotlinx.json)
            }
        }

        androidMain {
            dependencies {
                implementation(universe.ktor.client.android)
            }
        }

        jvmMain {
            dependencies {
                implementation(universe.ktor.client.java)
            }
        }

        jsMain {
            dependencies {
                api(universe.ktor.client.js)
            }
        }

        iosMain {
            dependencies {
                implementation(universe.ktor.client.ios)
            }
        }

        linuxMain {
            dependencies {
                implementation(universe.ktor.client.curl)
            }
        }
    }
}
