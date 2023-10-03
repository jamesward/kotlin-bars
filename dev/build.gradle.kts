plugins {
    `java-library`
    alias(universeunstable.plugins.kotlin.jvm)
}

repositories {
    mavenCentral()
}

dependencies {
    api(universe.testcontainers.postgresql)
    runtimeOnly(universe.slf4j.simple)
    runtimeOnly(universe.postgresql)
}

kotlin {
    jvmToolchain(11)
}
