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
        getByName("commonMain") {
            dependencies {
                api(kotlin("stdlib"))
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.4.1")
                api("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
            }
        }
        getByName("jvmMain") {
            dependencies {
                implementation("org.springframework.data:spring-data-commons:3.0.0")
            }
        }
        create("iosMain") {
            dependsOn(getByName("commonMain"))
        }
        getByName("iosX64Main") {
            dependsOn(getByName("iosMain"))
        }
        getByName("iosArm64Main") {
            dependsOn(getByName("iosMain"))
        }
        getByName("iosX64Main") {
            dependsOn(getByName("iosMain"))
        }
        getByName("iosSimulatorArm64Main") {
            dependsOn(getByName("iosMain"))
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
