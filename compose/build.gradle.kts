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
    android()

    jvm {

    }

    /*
    js(IR) {
        browser()
    }
     */

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
                implementation(kotlin("reflect"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1")

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
    compileSdk = 31

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].java.srcDirs("src/commonMain/kotlin")

    defaultConfig {
        minSdk = 28
    }

    /*
    lintOptions {
        disable("ObsoleteLintCustomCheck")
    }
     */
}
