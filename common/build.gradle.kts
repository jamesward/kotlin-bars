plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
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

    js(IR) {
        browser()
    }

    ios()

    iosSimulatorArm64()

    linuxX64()

    sourceSets {
        getByName("commonMain") {
            dependencies {
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
            }
        }
        getByName("jvmMain") {
            dependencies {
                implementation("org.springframework.data:spring-data-commons:3.1.1")
            }
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
