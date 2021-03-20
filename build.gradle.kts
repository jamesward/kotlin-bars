plugins {
    id("com.android.application") version "4.1.0" apply false
    kotlin("android") version "1.4.31" apply false
    kotlin("jvm") version "1.4.31" apply false
    kotlin("js") version "1.4.31" apply false
    kotlin("multiplatform") version "1.4.31" apply false
    kotlin("plugin.allopen") version "1.4.31" apply false
    kotlin("plugin.serialization") version "1.4.31" apply false
    id("io.micronaut.application") version "1.4.2" apply false
}

allprojects {
    repositories {
        mavenLocal()
        mavenCentral()
        jcenter()
        google()
    }
}