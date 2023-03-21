import java.util.Properties

plugins {
    application
    kotlin("jvm")
    id("com.palantir.graal") version "0.12.0"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":common"))

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.5.0")

    testImplementation(project(":dev"))
}

kotlin {
    jvmToolchain(17)
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
    graalVersion("22.3.0")
    javaVersion("17")
    mainClass(application.mainClass.get())
    outputName("kotlin-bars")
    option("--verbose")
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
