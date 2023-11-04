plugins {
    alias(universe.plugins.kotlin.multiplatform)
    alias(universe.plugins.kotlin.plugin.serialization)
    alias(universeunstable.plugins.android.kotlin.multiplatform.library)
}

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

    @Suppress("OPT_IN_USAGE")
    wasmJs {
        browser()
    }

    iosArm64()
    iosSimulatorArm64()

    linuxX64()

    sourceSets {
        commonMain {
            dependencies {
                api(project(":common"))

                api(universe.ktor.client.core)
                implementation(universe.ktor.client.content.negotiation)
                implementation(universe.ktor.serialization.kotlinx.json)
            }
        }

        androidMain {
            dependencies {
                //implementation(universe.ktor.client.android)
            }
        }

        jvmMain {
            dependencies {
                //implementation(universe.ktor.client.java)
            }
        }

        jsMain {
            dependencies {
                //api(universe.ktor.client.js)
            }
        }

        named("wasmJsMain") {
            dependencies {
                //implementation("org.jetbrains.kotlinx:kotlinx-serialization-core-wasm:1.5.1-wasm0")
                //implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-wasm:1.6.4-wasm0")
                //implementation("io.ktor:ktor-client-core-wasm:2.3.1-wasm0")
            }
        }

        iosMain {
            dependencies {
                //implementation(universe.ktor.client.ios)
            }
        }

        linuxMain {
            dependencies {
                //implementation("io.ktor:ktor-client-core-linuxx64:2.3.5")
                //implementation(universe.ktor.client.curl)
            }
        }
    }
}

configurations.all {
    resolutionStrategy {
        eachDependency {
            if (name.startsWith("wasmJs") && requested.module.group == "io.ktor") {
                useVersion("3.0.0-wasm1")
            }
        }
    }
}
