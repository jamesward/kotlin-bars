plugins {
    `java-library`
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

dependencies {
    api("org.testcontainers:postgresql:1.19.1")
    runtimeOnly("org.slf4j:slf4j-simple:2.0.9")
    runtimeOnly("org.postgresql:postgresql:42.6.0")
}

kotlin {
    jvmToolchain(11)
}
