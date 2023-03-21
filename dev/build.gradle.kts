plugins {
    `java-library`
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

dependencies {
    api("org.testcontainers:postgresql:1.17.6")
    runtimeOnly("org.slf4j:slf4j-simple:1.7.32")
    runtimeOnly("org.postgresql:postgresql:42.5.1")
}

kotlin {
    jvmToolchain(11)
}
