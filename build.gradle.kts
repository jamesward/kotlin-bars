// Android plugin 4.2 not compatible with IntelliJ yet
// Compose plugin not compatible with Kotlin 1.5 yet
plugins {
    id("com.android.application")           version "4.1.1" apply false
    id("org.jetbrains.compose")             version "0.4.0-build198" apply false
    id("org.mikeneck.graalvm-native-image") version "1.4.0" apply false
    kotlin("android")                       version "1.4.32" apply false
    kotlin("jvm")                           version "1.4.32" apply false
    kotlin("js")                            version "1.4.32" apply false
    kotlin("multiplatform")                 version "1.4.32" apply false
    kotlin("plugin.serialization")          version "1.4.32" apply false
    kotlin("plugin.spring")                 version "1.4.32" apply false
}

subprojects {
    repositories {
        mavenCentral()
        google()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}
