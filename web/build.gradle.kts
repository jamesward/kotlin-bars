import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(universe.plugins.kotlin.multiplatform)
    //alias(universe.plugins.jetbrains.compose)
    id("org.jetbrains.compose")
}

kotlin {
    /*
    @Suppress("OPT_IN_USAGE")
    wasmJs {
        browser {
            commonWebpackConfig {
                outputFileName = "kotlinbarswasmapp.js"
            }
            runTask(
                Action {
                    // todo: testcontainer server

                    // todo: external proxy fails with ERR_TLS_CERT_ALTNAME_INVALID
                    /*
                    val barsUrl: String? by project

                    val props = Properties()
                    rootProject.file("local.properties").let {
                        if (it.exists()) it.inputStream().use(props::load)
                    }

                    val barsUrlWithFallback = barsUrl ?: props["barsUrl"] as String?

                    val proxyUrl = if (barsUrlWithFallback != null) {
                        barsUrlWithFallback
                    }
                    else {
                        "http://localhost:8080"
                    }
                     */
                    devServer = devServer?.copy(port = 8081, proxy = mutableMapOf("/api" to "http://localhost:8080"))
                }
            )
        }
        binaries.executable()
    }

     */

    js(IR) {
        browser {
            /*
            commonWebpackConfig {
                outputFileName = "kotlinbarsjsapp.js"
                configDirectory = file("does-not-exist-because-we-do-not-want-to-share-with-wasm")
            }
             */
            runTask(
                Action {
                    // todo: testcontainer server

                    // todo: external proxy fails with ERR_TLS_CERT_ALTNAME_INVALID
                    /*
                    val barsUrl: String? by project

                    val props = Properties()
                    rootProject.file("local.properties").let {
                        if (it.exists()) it.inputStream().use(props::load)
                    }

                    val barsUrlWithFallback = barsUrl ?: props["barsUrl"] as String?

                    val proxyUrl = if (barsUrlWithFallback != null) {
                        barsUrlWithFallback
                    }
                    else {
                        "http://localhost:8080"
                    }
                     */
                    devServer = devServer?.copy(port = 8081, proxy = mutableMapOf("/api" to "http://localhost:8080"))
                }
            )
        }
        binaries.executable()
    }

    sourceSets {
        /*
        commonMain {
            dependencies {
                runtimeOnly(compose.runtime)
            }
        }

        named("wasmJsMain") {
            dependencies {
                implementation(project(":compose"))
            }
        }
         */

        jsMain {
            dependencies {
                implementation(project(":rpc"))

                implementation(compose.html.core)
                implementation(compose.runtime)
            }
        }
    }

}

/*
compose.experimental {
    web.application {

    }
}
 */

configurations.all {
    resolutionStrategy {
        eachDependency {
            if (name.startsWith("wasmJs") && requested.module.group == "io.ktor") {
                useVersion("3.0.0-wasm1")
            }
            if (requested.module.name == "html-core") {
                useVersion("1.5.10")
            }
        }
    }
}
