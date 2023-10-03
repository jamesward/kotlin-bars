plugins {
    id("com.android.application")                  version "8.3.0-alpha06" apply false
    id("com.android.library")                      version "8.3.0-alpha06" apply false
    id("com.android.kotlin.multiplatform.library") version "8.3.0-alpha06" apply false
    id("org.jetbrains.compose")                    version "1.5.10-beta02" apply false
    id("org.springframework.boot")                 version "3.1.4" apply false
    id("io.spring.dependency-management")          version "1.1.3" apply false
    id("org.graalvm.buildtools.native")            version "0.9.27" apply false
    kotlin("android")                              version "1.9.20-Beta2" apply false
    kotlin("jvm")                                  version "1.9.20-Beta2" apply false
    kotlin("multiplatform")                        version "1.9.20-Beta2" apply false
    kotlin("plugin.serialization")                 version "1.9.20-Beta2" apply false
    kotlin("plugin.spring")                        version "1.9.20-Beta2" apply false
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}
