plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("com.android.library")
}

kotlin {
    android()

    jvm()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
                implementation(kotlin("reflect"))

                api(project(":common"))

                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)

                implementation("io.ktor:ktor-client-core:1.5.2")
                implementation("io.ktor:ktor-client-json:1.5.2")
                implementation("io.ktor:ktor-client-gson:1.5.2")
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("io.ktor:ktor-client-android:1.5.2")
            }
        }
        val jvmMain by getting {
            dependencies {
                /*
                implementation("io.ktor:ktor-client-okhttp:1.5.2")
                 */
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

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}
