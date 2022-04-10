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

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(project(":common"))

                implementation("io.ktor:ktor-client-core:2.0.0") {
                    exclude("org.jetbrains.kotlin")
                }
                implementation("io.ktor:ktor-client-content-negotiation:2.0.0") {
                    exclude("org.jetbrains.kotlin")
                }
                implementation("io.ktor:ktor-serialization-kotlinx-json:2.0.0") {
                    exclude("org.jetbrains.kotlin")
                }
            }
        }

        val androidMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-android:2.0.0") {
                    exclude("org.jetbrains.kotlin")
                }
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-java:2.0.0") {
                    exclude("org.jetbrains.kotlin")
                }
            }
        }

        val jsMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-js:2.0.0") {
                    exclude("org.jetbrains.kotlin")
                }
            }
        }

        val iosMain by creating {
            dependsOn(commonMain)
            dependencies {
                implementation("io.ktor:ktor-client-ios:2.0.0") {
                    exclude("org.jetbrains.kotlin")
                }
            }
        }

        val iosX64Main by getting {
            dependsOn(iosMain)
        }
        val iosArm64Main by getting {
            dependsOn(iosMain)
        }
        val iosSimulatorArm64Main by getting {
            dependsOn(iosMain)
        }
    }
}

android {
    compileSdk = 31
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 24
        targetSdk = 31
    }
    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_11)
        targetCompatibility(JavaVersion.VERSION_11)
    }
}
