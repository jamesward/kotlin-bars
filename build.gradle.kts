plugins {
    id("com.android.application")              version "7.3.0" apply false
    id("com.android.library")                  version "7.3.0" apply false
    id("org.jetbrains.compose")                version "1.3.0-beta03" apply false
    id("org.springframework.boot")             version "3.0.0" apply false
    id("io.spring.dependency-management")      version "1.1.0" apply false
    id("org.graalvm.buildtools.native")        version "0.9.18" apply false
    kotlin("android")                          version "1.7.20" apply false
    kotlin("jvm")                              version "1.7.20" apply false
    kotlin("multiplatform")                    version "1.7.20" apply false
    kotlin("plugin.serialization")             version "1.7.20" apply false
    kotlin("plugin.spring")                    version "1.7.20" apply false
}

allprojects {
    repositories {
        mavenCentral()
        google()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}
