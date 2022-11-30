import org.gradle.api.tasks.testing.logging.TestLogEvent.*

plugins {
    application
    kotlin("jvm")
    kotlin("plugin.spring")
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    //id("org.graalvm.buildtools.native") version "0.9.18"
}

dependencies {
    implementation(project(":common"))
    //implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    //implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.6.4")
    //implementation("com.google.code.findbugs:annotations:3.0.1")

    //implementation("org.springframework.boot:spring-boot-starter-webflux")
    // instead of spring-boot-starter-webflux due to jackson transitive dep
    implementation("org.springframework:spring-webflux")
    implementation("org.springframework.boot:spring-boot-starter-reactor-netty")
    implementation("org.springframework.boot:spring-boot-starter")
    //implementation("org.springframework.boot:spring-boot-starter-actuator")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")

    runtimeOnly("org.postgresql:r2dbc-postgresql")

    testImplementation("org.springframework.boot:spring-boot-starter-test")

    testImplementation("org.testcontainers:postgresql:1.17.6")
    testImplementation("org.testcontainers:r2dbc:1.17.6")
    // for testcontainers to run the schema setup
    //testRuntimeOnly("org.postgresql:postgresql")

    // see: https://github.com/spring-projects-experimental/spring-native/issues/532
    //developmentOnly("org.springframework.boot:spring-boot-devtools")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
    }
}

application {
    mainClass.set("kotlinbars.server.MainKt")
}

// add the test stuff to the bootRun classpath
tasks.withType<org.springframework.boot.gradle.tasks.run.BootRun> {
    classpath = sourceSets["test"].runtimeClasspath
}

tasks.withType<Test> {
    useJUnitPlatform()

    testLogging {
        showStandardStreams = true
        exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
        events(STARTED, PASSED, SKIPPED, FAILED)
    }
}

tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootBuildImage> {
    builder.set("paketobuildpacks/builder:tiny")
    environment.set(mapOf("BP_NATIVE_IMAGE" to "1", "BP_JVM_VERSION" to "17", "BP_BINARY_COMPRESSION_METHOD" to "upx", "BP_NATIVE_IMAGE_BUILD_ARGUMENTS" to "--trace-object-instantiation=ch.qos.logback.classic.Logger"))
}
