import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import java.util.Properties

plugins {
    alias(universe.plugins.kotlin.multiplatform)
    alias(universe.plugins.palantir.graal)
}

kotlin {
    jvmToolchain(17)

    jvm {
        withJava()

        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        mainRun {
            mainClass = "kotlinbars.cli.MainKt"
        }
    }

    sourceSets {
        jvmMain {
            dependencies {
                implementation(project(":common"))

                implementation(universe.kotlinx.serialization.json.jvm)
            }
        }
        jvmTest {
            dependencies {
                implementation(project(":dev"))
            }
        }
    }
}

val generatedResourceDir = layout.buildDirectory.dir("generated-resources/main").get()

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
            val metaInf = generatedResourceDir.dir("META-INF")
            metaInf.asFile.mkdirs()
            val generated = metaInf.file("app.properties").asFile
            generated.writeText("barsUrl=$barsUrlWithFallback")
        } else {
            generatedResourceDir.asFile.deleteRecursively()
        }
    }
}

graal {
    graalVersion("22.3.0")
    javaVersion("17")
    mainClass("kotlinbars.cli.MainKt")
    outputName("kotlin-bars")
    option("--verbose")
    option("--no-fallback")
    option("-H:+ReportExceptionStackTraces")
    option("-H:IncludeResources=META-INF/app.properties")
    option("--enable-http")
    option("--enable-https")
}

tasks.withType<JavaExec> {
    standardInput = System.`in`
}

// todo: this rebuilds the server container every run
tasks.register<JavaExec>("dev") {
    dependsOn(":server:bootBuildImage")
    dependsOn("jvmTestClasses")
    classpath = kotlin.jvm().compilations["test"].runtimeDependencyFiles
    mainClass = "kotlinbars.cli.DevKt"
    standardInput = System.`in`
}
