plugins {
    alias(universe.plugins.kotlin.multiplatform)
    alias(universe.plugins.kotlin.plugin.serialization)
    //alias(universeunstable.plugins.android.kotlin.multiplatform.library)
    id("com.android.kotlin.multiplatform.library")
}

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
                // todo: to version catalog?
                api("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.1-wasm1")
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
