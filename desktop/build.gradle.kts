import java.util.Properties
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
    jvm()

    sourceSets {
        jvmMain {
            dependencies {
                implementation(project(":compose"))
                implementation(compose.desktop.currentOs)
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

tasks.named<Copy>("jvmProcessResources") {
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

compose.desktop {
    application {
        mainClass = "kotlinbars.desktop.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            modules("java.net.http")
            modules("jdk.crypto.ec")
            packageName = "Kotlin Bars"
            packageVersion = System.getenv()["REF"]?.removePrefix("refs/tags/v") ?: "255.255.65535"
            macOS {
                bundleID = System.getenv()["ASC_BUNDLE_ID"]
                signing {
                    sign = System.getenv()["DESKTOP_CERT_NAME"] != null
                    identity = System.getenv()["DESKTOP_CERT_NAME"]
                }
                notarization {
                    appleID = System.getenv()["NOTARIZATION_APPLEID"]
                    password = System.getenv()["NOTARIZATION_PASSWORD"]
                }
            }
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
    mainClass = "kotlinbars.desktop.DevKt"
}
