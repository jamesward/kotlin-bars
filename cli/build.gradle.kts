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

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.12.3")

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

// todo: this rebuilds the server container every run
tasks.register<JavaExec>("dev") {
    dependsOn(":server:bootBuildImage")
    dependsOn("testClasses")
    classpath = sourceSets["test"].runtimeClasspath
    main = "kotlinbars.cli.DevKt"
    standardInput = System.`in`
}
