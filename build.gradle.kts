plugins {
    id("com.android.application")              version "7.4.0-beta02" apply false // latest for IntelliJ compat
    id("com.android.library")                  version "7.4.0-beta02" apply false // latest for IntelliJ compat
    id("org.jetbrains.compose")                version "1.3.1" apply false
    id("org.springframework.boot")             version "3.0.4" apply false
    id("io.spring.dependency-management")      version "1.1.0" apply false
    id("org.graalvm.buildtools.native")        version "0.9.20" apply false
    kotlin("android")                          version "1.8.10" apply false
    kotlin("jvm")                              version "1.8.10" apply false
    kotlin("multiplatform")                    version "1.8.10" apply false
    kotlin("plugin.serialization")             version "1.8.10" apply false
    kotlin("plugin.spring")                    version "1.8.10" apply false
}

allprojects {
    repositories {
        mavenCentral()
        google()
        //maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}
