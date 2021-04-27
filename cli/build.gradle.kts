plugins {
    application
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":common"))
    implementation(kotlin("stdlib-jdk8"))
    //implementation(kotlin("reflect"))
    //implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.4.3")

    //implementation("org.springframework.boot:spring-boot-starter-webflux")
    //implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.3")

    testImplementation("org.testcontainers:postgresql:1.15.3")
    testRuntimeOnly("org.slf4j:slf4j-simple:1.7.30")
    testRuntimeOnly("org.postgresql:postgresql:42.2.19")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = JavaVersion.VERSION_11.toString()
    }
}

application {
    mainClass.set("kotlinbars.cli.MainKt")
}

// todo: this rebuilds the server container every run
tasks.register<JavaExec>("dev") {
    //dependsOn(":server:bootBuildImage")
    dependsOn("testClasses")
    classpath = sourceSets["test"].runtimeClasspath
    main = "kotlinbars.cli.DevKt"
    standardInput = System.`in`
}
