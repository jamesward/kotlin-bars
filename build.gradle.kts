plugins {
    id("com.android.application") version "4.1.1"  apply false
    id("org.jetbrains.compose")   version "0.3.2"  apply false
    kotlin("android")             version "1.4.31" apply false
    kotlin("jvm")                 version "1.4.31" apply false
    kotlin("js")                  version "1.4.31" apply false
    kotlin("multiplatform")       version "1.4.31" apply false
    kotlin("plugin.spring")       version "1.4.31" apply false
}

subprojects {
    repositories {
        mavenCentral()
        google()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}
