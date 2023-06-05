plugins {
    `java-library`
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

dependencies {
    api("org.testcontainers:postgresql:1.18.3")
    runtimeOnly("org.slf4j:slf4j-simple:1.7.32")
    runtimeOnly("org.postgresql:postgresql:42.6.0")
}

kotlin {
    jvmToolchain(11)
}
