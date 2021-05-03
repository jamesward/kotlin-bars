import java.util.Properties

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
        props.load(rootProject.file("local.properties").inputStream())

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

nativeImage {
    graalVmHome = System.getenv()["GRAALVM_HOME"] ?: ""
    mainClass = application.mainClass.get()
    executableName = "kotlin-bars-cli"
    arguments(
        "--no-fallback",
        "--verbose",
        "--enable-http",
        "--enable-https",
        "-H:IncludeResources=META-INF/app.properties",
    )
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}

// todo: this rebuilds the server container every run
tasks.register<JavaExec>("dev") {
    dependsOn(":server:bootBuildImage")
    dependsOn("testClasses")
    classpath = sourceSets["test"].runtimeClasspath
    main = "kotlinbars.cli.DevKt"
    standardInput = System.`in`
}
