import java.util.Properties
import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
    }

    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib"))
                implementation(project(":compose"))
                implementation(compose.desktop.currentOs)
                val os = System.getProperty("os.name")
                val currentTarget = when {
                    os.equals("Mac OS X", ignoreCase = true) -> "macos"
                    os.startsWith("Win", ignoreCase = true) -> "windows"
                    os.startsWith("Linux", ignoreCase = true) -> "linux"
                    else -> error("Unknown OS name: $os")
                }
                runtimeOnly("org.jetbrains.skiko:skiko-jvm-runtime-${currentTarget}-x64:0.2.33")
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(project(":dev"))
            }
        }
    }
}

val generatedResourceDir = File("$buildDir/generated-resources/main")

tasks.named<Copy>("jvmProcessResources") {
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

compose.desktop {
    application {
        mainClass = "kotlinbars.desktop.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            modules("java.net.http")
            modules("jdk.crypto.ec")
            packageVersion = "1.0.0"
        }
    }
}

tasks.register<JavaExec>("dev") {
    dependsOn(":server:bootBuildImage")
    kotlin {
        val test = targets["jvm"].compilations["test"]
        classpath += configurations["jvmTestRuntimeClasspath"]
        classpath += test.output.allOutputs
    }
    main = "kotlinbars.desktop.DevKt"
}
