plugins {
    kotlin("jvm")
    kotlin("kapt")
    kotlin("plugin.allopen")
    id("io.micronaut.application")
}

dependencies {
    implementation(project(":common"))
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.4.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-rx2:1.4.3")

    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")

    runtimeOnly("ch.qos.logback:logback-classic")
    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")

    implementation("io.micronaut.sql:micronaut-jasync-sql")
    implementation("com.github.jasync-sql:jasync-postgresql:1.1.5")

    kapt("io.micronaut:micronaut-graal")

    /*
    kapt("io.micronaut:micronaut-inject-java")
    kapt("io.micronaut.data:micronaut-data-processor")
     */

    //implementation("io.micronaut.data:micronaut-data-hibernate-jpa")

    testImplementation("org.testcontainers:postgresql:1.15.2")
    // for testcontainers to run the schema setup
    testRuntimeOnly("org.postgresql:postgresql:42.2.6")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks.compileKotlin {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
        javaParameters = true
    }
}

micronaut {
    version.set("2.4.1")
    runtime("netty")
    processing {
        incremental(true)
        annotations("kotlinbars.*")
    }
}

application {
    mainClass.set("kotlinbars.WebAppKt")
}

tasks.register<JavaExec>("testRun") {
    dependsOn("testClasses")
    classpath = sourceSets["test"].runtimeClasspath
    main = "kotlinbars.WebAppKt"
    systemProperty("micronaut.environments", "test")
    systemProperty("micronaut.server.port", "8080")
}

tasks.register<JavaExec>("graalRun") {
    systemProperty("micronaut.environments", "test")
    systemProperty("micronaut.server.port", "8080")
    jvmArgs("-agentlib:native-image-agent=config-output-dir=src/graal")
}

tasks {
    dockerBuildNative {
        images.set(listOf("kotlin-bars-server"))
    }
    nativeImage {
        args("--verbose")
        //args("--static")
        args("--initialize-at-build-time=kotlin.ULong")
    }
}