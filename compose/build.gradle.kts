plugins {
    alias(universe.plugins.kotlin.multiplatform)
    //alias(universe.plugins.jetbrains.compose)
    id("org.jetbrains.compose")
    alias(universeunstable.plugins.android.kotlin.multiplatform.library)
}

kotlin {
    jvmToolchain(11)

    androidLibrary {
        @Suppress("UnstableApiUsage")
        namespace = "kotlinbars.compose"
        @Suppress("UnstableApiUsage")
        compileSdk = 34
    }

    jvm()

    @Suppress("OPT_IN_USAGE")
    wasmJs {
        browser()
    }

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

                api(compose.material3)
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
