plugins {
    id("com.android.application")           version "7.0.2" apply false
    id("org.jetbrains.compose")             version "1.1.0-alpha1-dev536" apply false
    kotlin("android")                       version "1.6.10" apply false
    kotlin("jvm")                           version "1.6.10" apply false
    kotlin("js")                            version "1.6.10" apply false
    kotlin("multiplatform")                 version "1.6.10" apply false
    kotlin("plugin.serialization")          version "1.6.10" apply false
    kotlin("plugin.spring")                 version "1.6.10" apply false
}

subprojects {
    repositories {
        mavenCentral()
        google()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}
