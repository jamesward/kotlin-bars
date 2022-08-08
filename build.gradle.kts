plugins {
    id("com.android.application")              version "7.2.2" apply false
    id("com.android.library")                  version "7.2.2" apply false
    id("org.jetbrains.compose")                version "1.2.0-alpha01-dev753" apply false
    id("org.springframework.boot")             version "2.7.2" apply false
    id("io.spring.dependency-management")      version "1.0.12.RELEASE" apply false
    id("org.springframework.experimental.aot") version "0.12.1" apply false
    kotlin("android")                          version "1.7.0" apply false
    kotlin("jvm")                              version "1.7.0" apply false
    kotlin("multiplatform")                    version "1.7.0" apply false
    kotlin("plugin.serialization")             version "1.7.0" apply false
    kotlin("plugin.spring")                    version "1.7.0" apply false
}

allprojects {
    repositories {
        mavenCentral()
        google()
        maven("https://repo.spring.io/release")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}
