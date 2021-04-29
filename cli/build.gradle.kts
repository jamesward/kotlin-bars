plugins {
    application
    kotlin("jvm")
    id("org.mikeneck.graalvm-native-image")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":common"))
    implementation(kotlin("stdlib-jdk8"))

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.1.0")

    testImplementation(project(":dev"))
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

application {
    mainClass.set("kotlinbars.cli.MainKt")
}

nativeImage {
    graalVmHome = System.getenv()["GRAALVM_HOME"] ?: ""
    mainClass = application.mainClass.get()
    executableName = "kotlin-bars-cli"
    arguments(
        "--no-fallback",
        "--verbose",
        "--enable-http",
        "--enable-https",
    )
}

// todo: this rebuilds the server container every run
tasks.register<JavaExec>("dev") {
    dependsOn(":server:bootBuildImage")
    dependsOn("testClasses")
    classpath = sourceSets["test"].runtimeClasspath
    main = "kotlinbars.cli.DevKt"
    standardInput = System.`in`
}
