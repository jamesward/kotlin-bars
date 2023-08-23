plugins {
    id("com.android.application")              version "8.0.0" apply false
    id("com.android.library")                  version "8.1.1" apply false
    id("org.jetbrains.compose")                version "1.4.3" apply false
    id("org.springframework.boot")             version "3.1.2" apply false
    id("io.spring.dependency-management")      version "1.1.3" apply false
    id("org.graalvm.buildtools.native")        version "0.9.24" apply false
    kotlin("android")                          version "1.9.0" apply false
    kotlin("jvm")                              version "1.9.0" apply false
    kotlin("multiplatform")                    version "1.9.0" apply false
    kotlin("plugin.serialization")             version "1.9.0" apply false
    kotlin("plugin.spring")                    version "1.9.0" apply false
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}
