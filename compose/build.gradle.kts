plugins {
    alias(universe.plugins.kotlin.multiplatform)
    alias(universe.plugins.jetbrains.compose)
    alias(universe.plugins.android.kotlin.multiplatform.library)
}

@OptIn(org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi::class)
kotlin {
    jvmToolchain(11)

    androidLibrary {
        @Suppress("UnstableApiUsage")
        namespace = "kotlinbars.compose"
        @Suppress("UnstableApiUsage")
        compileSdk = 34
    }

    jvm()

    iosArm64 {
        binaries.framework {
            isStatic = true
            baseName = "KotlinbarsCompose"
        }
    }

    iosSimulatorArm64 {
        binaries.framework {
            isStatic = true
            baseName = "KotlinbarsCompose"
        }
    }

    sourceSets {
        commonMain {
            dependencies {
                api(project(":rpc"))

                implementation(compose.ui)
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
            }
        }
    }
}
