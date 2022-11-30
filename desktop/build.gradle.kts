import java.util.Properties
import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(11))
    }
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = JavaVersion.VERSION_11.toString()
        }
    }

    sourceSets {
        named("jvmMain") {
            dependencies {
                implementation(kotlin("stdlib"))
                implementation(project(":compose"))
                implementation(compose.desktop.currentOs)
            }
        }

        named("jvmTest") {
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
                    sign.set(System.getenv()["DESKTOP_CERT_NAME"] != null)
                    identity.set(System.getenv()["DESKTOP_CERT_NAME"])
                }
                notarization {
                    appleID.set(System.getenv()["NOTARIZATION_APPLEID"])
                    password.set(System.getenv()["NOTARIZATION_PASSWORD"])
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
    mainClass.set("kotlinbars.desktop.DevKt")
}
