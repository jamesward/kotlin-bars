plugins {
    id("com.android.application")              version "7.4.2" apply false // latest for IntelliJ & KMP compat
    id("com.android.library")                  version "8.0.2" apply false // latest for IntelliJ & KMP compat
    id("org.jetbrains.compose")                version "1.4.0" apply false
    id("org.springframework.boot")             version "3.1.0" apply false
    id("io.spring.dependency-management")      version "1.1.0" apply false
    id("org.graalvm.buildtools.native")        version "0.9.20" apply false
    kotlin("android")                          version "1.8.20" apply false // latest for Compose
    kotlin("jvm")                              version "1.8.20" apply false // latest for Compose
    kotlin("multiplatform")                    version "1.8.20" apply false // latest for Compose
    kotlin("plugin.serialization")             version "1.8.20" apply false // latest for Compose
    kotlin("plugin.spring")                    version "1.8.20" apply false // latest for Compose
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}
