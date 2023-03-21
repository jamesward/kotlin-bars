import java.util.Properties

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
        named("commonMain") {
            dependencies {
                implementation(project(":rpc"))
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
                implementation("com.varabyte.kotter:kotter:1.1.0-rc3")
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

        val props = Properties()
        rootProject.file("local.properties").let {
            if (it.exists()) it.inputStream().use(props::load)
        }

        val barsUrlWithFallback = barsUrl ?: props["barsUrl"] as String? ?: "http://localhost:8080/api/bars"

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
