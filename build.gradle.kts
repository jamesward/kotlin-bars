plugins {
    application
    kotlin("jvm")                              version "1.5.21"
    kotlin("plugin.serialization")             version "1.5.21"
    kotlin("plugin.spring")                    version "1.5.21"
    id("org.springframework.boot")             version "2.5.3"
    id("io.spring.dependency-management")      version "1.0.11.RELEASE"
    id("org.springframework.experimental.aot") version "0.10.2"
    id("org.graalvm.buildtools.native")        version "0.9.1"
}

repositories {
    maven(uri("https://repo.spring.io/release"))
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

    //implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.4.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8")

    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-actuator")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("io.r2dbc:r2dbc-postgresql")

    testImplementation("org.testcontainers:postgresql:1.15.3")
    // for testcontainers to run the schema setup
    testRuntimeOnly("org.postgresql:postgresql")

    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.3")
    implementation("org.webjars:bootstrap:4.5.3")

    // see: https://github.com/spring-projects-experimental/spring-native/issues/532
    //developmentOnly("org.springframework.boot:spring-boot-devtools")
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
    mainClass.set("kotlinbars.MainKt")
}

// add the aot stuff to the run classpath
tasks.named<JavaExec>("run") {
    classpath += sourceSets["aot"].runtimeClasspath
}

// workaround an aot plugin incompatiblity with gradle 7
tasks.named<Copy>("processAotTestResources") {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

// add the test stuff to the bootRun classpath
tasks.withType<org.springframework.boot.gradle.tasks.run.BootRun> {
    classpath += sourceSets["test"].runtimeClasspath
}

tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootBuildImage> {
    val args = setOf(
        "-Dspring.spel.ignore=true",
        "-Dspring.native.remove-yaml-support=true"
    )
    builder = "paketobuildpacks/builder:tiny"
    environment = mapOf(
        "BP_NATIVE_IMAGE" to "1",
        "BP_NATIVE_IMAGE_BUILD_ARGUMENTS" to args.joinToString(" ")
    )
}
