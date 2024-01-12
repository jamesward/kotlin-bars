import org.gradle.api.tasks.testing.logging.TestLogEvent.*

plugins {
    application
    alias(universe.plugins.spring.boot)
    //alias(universe.plugins.spring.dependency.management)
    //alias(universe.plugins.kotlin.jvm)

    // work around for version mismatch
    id(universe.plugins.kotlin.jvm.map { it.pluginId }.get())

    // https://youtrack.jetbrains.com/issue/KTIJ-26692/Combination-of-kotlin-multiplatform-and-spring-boot-throws-java.lang.ClassNotFoundException
    //alias(universe.plugins.kotlin.multiplatform)

    alias(universe.plugins.kotlin.plugin.spring)
    // workaround: has to be last for https://github.com/spring-projects/spring-boot/issues/36488
    alias(universe.plugins.graalvm.buildtools.native)
}

kotlin {
    jvmToolchain(17)

    /*
    jvm {
        withJava()
    }

    sourceSets {
        jvmMain {
            dependencies {
                implementation(project(":common"))
                implementation(kotlin("reflect"))

                // instead of spring-boot-starter-webflux due to jackson transitive dep
                /*
                implementation("org.springframework:spring-webflux")
                implementation("org.springframework.boot:spring-boot-starter-reactor-netty")

                implementation("org.springframework.boot:spring-boot-starter")
                 */

                implementation(universe.spring.boot.starter.webflux)
                //implementation("org.springframework.boot:spring-boot-starter-actuator")

                implementation(universe.kotlinx.coroutines.reactor)

                implementation(universe.spring.boot.starter.data.r2dbc)

                runtimeOnly(universe.r2dbc.postgresql)
            }
        }
        jvmTest {
            dependencies {
                implementation(universe.spring.boot.starter.test)

                implementation(universe.testcontainers.postgresql)
                implementation(universe.testcontainers.r2dbc)
            }
        }
    }
     */
}

dependencies {
    implementation(project(":common"))
    implementation(kotlin("reflect"))

    // instead of spring-boot-starter-webflux due to jackson transitive dep
    /*
    implementation("org.springframework:spring-webflux")
    implementation("org.springframework.boot:spring-boot-starter-reactor-netty")

    implementation("org.springframework.boot:spring-boot-starter")
     */

    implementation(universe.spring.boot.starter.webflux)

    // for container health
    implementation(universe.spring.boot.starter.actuator)

    implementation(universe.kotlinx.coroutines.reactor)

    implementation(universe.spring.boot.starter.data.r2dbc)

    runtimeOnly(universe.r2dbc.postgresql)

    testImplementation(universe.spring.boot.starter.test)

    testImplementation(universe.testcontainers.postgresql)
    testImplementation(universe.testcontainers.r2dbc)
}

application {
    mainClass = "kotlinbars.server.MainKt"
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

tasks.withType<org.springframework.boot.gradle.tasks.aot.ProcessAot> {
    systemProperty("spring.r2dbc.url", "placeholder_for_aot")
}

tasks.withType<org.springframework.boot.gradle.tasks.bundling.BootBuildImage> {
    imageName = "kotlin-bars-server"
    environment = mapOf("BP_BINARY_COMPRESSION_METHOD" to "upx")
    if (System.getenv("DOCKER_HOST") != null) {
        docker {
            host = "inherit"
            bindHostToBuilder = true
        }
    }
}
