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
            baseName = "KotlinbarsCommon"
        }
    }

    iosArm64 {
        binaries.framework {
            baseName = "KotlinbarsCommon"
        }
    }

    iosSimulatorArm64 {
        binaries.framework {
            baseName = "KotlinbarsCommon"
        }
    }

    linuxX64 {

    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                api(kotlin("stdlib"))
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("org.springframework.data:spring-data-commons:2.7.2")
            }
        }
        val iosMain by creating {
            dependsOn(commonMain)
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

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.javaParameters = true // this is needed for Spring to reflect parameter names
}

android {
    namespace = "kotlinbars.common"
    compileSdk = 31
    defaultConfig {
        minSdk = 24
        targetSdk = 31
    }
    compileOptions {
        sourceCompatibility(JavaVersion.VERSION_11)
        targetCompatibility(JavaVersion.VERSION_11)
    }
}
