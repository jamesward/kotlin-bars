plugins {
    application
    kotlin("jvm")
    kotlin("plugin.spring")
    id("org.springframework.boot") version "2.4.4"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id("org.springframework.experimental.aot") version "0.9.0"
}

repositories {
    mavenCentral()
    maven("https://repo.spring.io/release")
}

dependencies {
    implementation(project(":common"))
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.4.3")

    implementation("org.springframework.boot:spring-boot-starter-webflux")

    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("io.r2dbc:r2dbc-postgresql")

    testImplementation("org.testcontainers:postgresql:1.15.2")
    // for testcontainers to run the schema setup
    testRuntimeOnly("org.postgresql:postgresql")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    //developmentOnly("org.springframework.boot:spring-boot-devtools")

    implementation("org.springframework.experimental:spring-native:0.9.0")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

application {
    mainClass.set("kotlinbars.MainKt")
}

tasks.withType<org.springframework.boot.gradle.tasks.run.BootRun> {
    dependsOn("testClasses")
    classpath = configurations["developmentOnly"] + sourceSets["test"].runtimeClasspath
}

tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootBuildImage> {
    val args = setOf(
        "-Dspring.spel.ignore=true",
        "-Dspring.native.remove-yaml-support=true"
    )
    builder = "paketobuildpacks/builder:tiny"
    environment = mapOf(
        "BP_BOOT_NATIVE_IMAGE" to "1",
        "BP_BOOT_NATIVE_IMAGE_BUILD_ARGUMENTS" to args.joinToString(" ")
    )
}
