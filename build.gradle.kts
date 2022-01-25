plugins {
    id("com.android.application")              version "7.0.4" apply false
    id("org.jetbrains.compose")                version "1.1.0-alpha1-dev536" apply false
    id("org.springframework.boot")             version "2.6.3" apply false
    id("io.spring.dependency-management")      version "1.0.11.RELEASE" apply false
    id("org.springframework.experimental.aot") version "0.11.1" apply false
    kotlin("android")                          version "1.6.10" apply false
    kotlin("jvm")                              version "1.6.10" apply false
    kotlin("js")                               version "1.6.10" apply false
    kotlin("multiplatform")                    version "1.6.10" apply false
    kotlin("plugin.serialization")             version "1.6.10" apply false
    kotlin("plugin.spring")                    version "1.6.10" apply false

}

allprojects {
    repositories {
        mavenCentral()
        google()
        maven("https://repo.spring.io/release")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}
