plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("com.android.library")
}

kotlin {
    android()

    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
                implementation(kotlin("reflect"))

                api(project(":common"))

                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)

                implementation("io.ktor:ktor-client-core:1.5.4")
                implementation("io.ktor:ktor-client-json:1.5.4")
                implementation("io.ktor:ktor-client-serialization:1.5.4")
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-android:1.5.4")
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-java:1.5.4")
            }
        }
    }
}

android {
    compileSdkVersion(30)

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].java.srcDirs("src/commonMain/kotlin")

    defaultConfig {
        minSdkVersion(28)
    }

    lintOptions {
        disable("ObsoleteLintCustomCheck")
    }
}
