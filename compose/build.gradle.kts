plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
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

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("reflect"))

                api(project(":rpc"))

                api(compose.runtime)
                api(compose.ui)
                api(compose.foundation)
                api(compose.material)
            }
        }
    }
}

android {
    namespace = "kotlinbars.compose"
    compileSdk = 31

    defaultConfig {
        minSdk = 28
    }
}
