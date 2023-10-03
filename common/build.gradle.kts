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
        namespace = "kotlinbars.common"
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
                api(universe.kotlinx.serialization.json)
            }
        }
        jvmMain {
            dependencies {
                implementation(universe.spring.data.commons)
            }
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.javaParameters = true // this is needed for Spring to reflect parameter names
}
