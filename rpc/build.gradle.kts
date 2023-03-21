plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("com.android.library")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

kotlin {
    android {

    }

    jvm {

    }

    js(IR) {
        browser()
    }

    iosX64 {
        binaries.framework {
            baseName = "KotlinbarsRpc"
        }
    }

    iosArm64 {
        binaries.framework {
            baseName = "KotlinbarsRpc"
        }
    }

    iosSimulatorArm64 {
        binaries.framework {
            baseName = "KotlinbarsRpc"
        }
    }

    linuxX64 {

    }

    sourceSets {
        val commonMain = getByName("commonMain") {
            dependencies {
                api(project(":common"))

                implementation("io.ktor:ktor-client-core:2.2.4")
                implementation("io.ktor:ktor-client-content-negotiation:2.2.4")
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.2.4")
            }
        }

        getByName("androidMain") {
            dependencies {
                implementation("io.ktor:ktor-client-android:2.2.4")
                implementation("androidx.core:core:1.9.0")
            }
        }

        getByName("jvmMain") {
            dependencies {
                implementation("io.ktor:ktor-client-java:2.2.4")
            }
        }

        getByName("jsMain") {
            dependencies {
                implementation("io.ktor:ktor-client-js:2.2.4")
            }
        }

        val iosMain = create("iosMain") {
            dependsOn(commonMain)
            dependencies {
                implementation("io.ktor:ktor-client-ios:2.2.4")
            }
        }

        getByName("iosX64Main") {
            dependsOn(iosMain)
        }

        getByName("iosArm64Main") {
            dependsOn(iosMain)
        }
        getByName("iosSimulatorArm64Main") {
            dependsOn(iosMain)
        }

        getByName("linuxX64Main") {
            dependencies {
                implementation("io.ktor:ktor-client-curl:2.2.4")
            }
        }
    }
}

android {
    namespace = "kotlinbars.rpc"
    compileSdk = 33
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_11)
        targetCompatibility(JavaVersion.VERSION_11)
    }
}
