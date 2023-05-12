import java.util.*

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
    js(IR) {
        browser {
            runTask {
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
        }
        binaries.executable()
    }

    sourceSets {
        named("jsMain") {
            dependencies {
                implementation(project(":rpc"))
                implementation(compose.html.core)
                implementation(compose.runtime)
            }
        }
    }

}
