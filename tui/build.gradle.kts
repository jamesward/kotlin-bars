plugins {
    kotlin("multiplatform")
}

repositories {
    mavenCentral()
}

kotlin {
    linuxX64 {
        binaries {
            executable(listOf(DEBUG, RELEASE)) {
                entryPoint = "kotlinbars.tui.main"
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":rpc"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.1-native-mt")
            }
        }

    }
}

tasks.withType<Exec> {
    standardInput = System.`in`
}

tasks.register("createConfig") {
    doLast {
        val barsUrl: String? by project

        // todo: local.properties

        val barsUrlWithFallback = barsUrl ?: "http://localhost:8080/api/bars"

        val configContents = """
            package kotlinbars.tui
            
            object Config {
                val barsUrl = "$barsUrlWithFallback"
            }
        """.trimIndent()

        val configFile = project.file("src/commonMain/kotlin/kotlinbars/tui/Config.kt")
        try { configFile.delete() } catch (_: Exception) { }
        configFile.createNewFile()
        configFile.writeText(configContents)
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinNativeCompile>().configureEach {
    dependsOn("createConfig")
}

// todo: this rebuilds the server container every run
// todo: this needs to run the server via the dev subproject and pass the connection info to the app
tasks.register<Exec>("dev") {
    dependsOn(":server:bootBuildImage", "runDebugExecutableLinuxX64")
}