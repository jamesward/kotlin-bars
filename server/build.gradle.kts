plugins {
    application
    kotlin("jvm")
    id("com.palantir.graal") version "0.7.2"
}

dependencies {
    implementation(project(":common"))
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.4.3")

    implementation("io.ktor:ktor-server-core:1.5.2")
    implementation("io.ktor:ktor-server-netty:1.5.2")
    implementation("io.ktor:ktor-jackson:1.5.2")

    runtimeOnly("io.netty:netty-handler:4.1.60.Final")
    runtimeOnly("io.netty:netty-transport:4.1.60.Final")

    implementation("com.github.jasync-sql:jasync-postgresql:1.1.7")

    //implementation("io.r2dbc:r2dbc-postgresql:0.8.7.RELEASE")

    runtimeOnly("ch.qos.logback:logback-classic:1.2.3")

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

application {
    mainClass.set("kotlinbars.MainKt")
}

tasks.register<JavaExec>("testRun") {
    dependsOn("testClasses")
    classpath = sourceSets["test"].runtimeClasspath
    main = "kotlinbars.TestMainKt"
}

/*
TODO:
./gradlew :ktor-server:extractGraalTooling
./gradlew :ktor-server:install
JAVA_HOME=~/.gradle/caches/com.palantir.graal/20.3.0/11/graalvm-ce-java11-20.3.0 \
  JAVA_OPTS=-agentlib:native-image-agent=config-output-dir=ktor-server/src/graal \
  ktor-server/build/install/ktor-server/bin/ktor-server
 */
tasks.register<JavaExec>("graalRun") {
    classpath = sourceSets["main"].runtimeClasspath
    main = "kotlinbars.MainKt"
    jvmArgs("-agentlib:native-image-agent=config-output-dir=src/graal")
}

graal {
    outputName("kotlin-bars-server")
    graalVersion("21.0.0.2")
    mainClass(application.mainClass.get())
    javaVersion("8")
    option("--verbose")
    option("--no-server")
    option("--no-fallback")
    option("-H:+ReportExceptionStackTraces")
    option("--allow-incomplete-classpath")
    option("-H:ConfigurationFileDirectories=src/graal")
    option("--initialize-at-build-time=org.slf4j.LoggerFactory")
    option("--initialize-at-build-time=org.slf4j.impl.StaticLoggerBinder")
    //option("--initialize-at-build-time=ch.qos.logback.classic.Logger")
    //option("--initialize-at-build-time=ch.qos.logback.classic.Level")
    option("--initialize-at-build-time=ch.qos.logback")
}