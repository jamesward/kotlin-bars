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
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(project(":dev"))
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "kotlinbars.desktop.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
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
