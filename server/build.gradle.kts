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

    //implementation("io.micronaut.sql:micronaut-jasync-sql")
    //implementation("com.github.jasync-sql:jasync-postgresql:1.1.7")
    implementation("io.micronaut.r2dbc:micronaut-r2dbc-rxjava2")
    implementation("io.micronaut.r2dbc:micronaut-data-r2dbc")
    runtimeOnly("io.r2dbc:r2dbc-postgresql:0.8.7.RELEASE")

    kapt("io.micronaut.data:micronaut-data-processor")
    /*
    kapt("io.micronaut:micronaut-graal")
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

kapt {
    arguments {
        arg("micronaut.processing.incremental", true)
    }
}

/*
application {
    mainClass.set("com.jamesward.airdraw.WebAppKt")
}

allOpen {
    annotation("io.micronaut.aop.Around")
}

kapt {
    arguments {
        arg("micronaut.processing.incremental", true)
        arg("micronaut.processing.annotations", "com.jamesward.airdraw.*")
    }
}
 */

/*
tasks.withType<JavaExec> {
jvmArgs = listOf("-XX:TieredStopAtLevel=1", "-Dcom.sun.management.jmxremote")

if (gradle.startParameter.isContinuous) {
    systemProperties = mapOf(
            "micronaut.io.watch.restart" to "true",
            "micronaut.io.watch.enabled" to "true",
            "micronaut.io.watch.paths" to "src/main"
    )
}
}
 */

/*
tasks {
    classes {
        dependsOn(":web:jsJar")
    }
}
 */