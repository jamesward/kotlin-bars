plugins {
    `java-library`
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    api("org.testcontainers:postgresql:1.15.3")
    runtimeOnly("org.slf4j:slf4j-simple:1.7.30")
    runtimeOnly("org.postgresql:postgresql:42.2.19")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
}
