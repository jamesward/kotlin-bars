import java.util.Properties

plugins {
    application
    kotlin("jvm")
    id("com.palantir.graal") version "0.10.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":common"))
    implementation(kotlin("stdlib-jdk8"))

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.3.1")

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

val generatedResourceDir = File("$buildDir/generated-resources/main")

tasks.named<Copy>("processResources") {
    from(tasks.named("generateResources"))
}

tasks.register("generateResources") {
    outputs.upToDateWhen { false }
    outputs.dir(generatedResourceDir)
    doLast {
        val barsUrl: String? by project

        val props = Properties()
        rootProject.file("local.properties").let {
            if (it.exists()) it.inputStream().use(props::load)
        }

        val barsUrlWithFallback = barsUrl ?: props["barsUrl"] as String?

        if (barsUrlWithFallback != null) {
            val metaInf = File(generatedResourceDir, "META-INF")
            metaInf.mkdirs()
            val generated = File(metaInf, "app.properties")
            generated.writeText("barsUrl=$barsUrlWithFallback")
        } else {
            generatedResourceDir.deleteRecursively()
        }
    }
}

graal {
    graalVersion("21.3.0")
    javaVersion("11")
    mainClass(application.mainClass.get())
    outputName("kotlin-bars")
    option("--verbose")
    option("--no-server")
    option("--no-fallback")
    option("-H:+ReportExceptionStackTraces")
    option("-H:IncludeResources=META-INF/app.properties")
    option("--enable-http")
    option("--enable-https")
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}

// todo: this rebuilds the server container every run
tasks.register<JavaExec>("dev") {
    dependsOn(":server:bootBuildImage")
    dependsOn("testClasses")
    classpath = sourceSets["test"].runtimeClasspath
    mainClass.set("kotlinbars.cli.DevKt")
    standardInput = System.`in`
}
