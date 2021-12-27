plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
    js(IR) {
        browser {
            runTask {
                devServer = devServer?.copy(port = 8081, proxy = mutableMapOf("/api" to "http://localhost:8080"))
            }
        }
        binaries.executable()
    }

    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation(project(":rpc"))
                implementation(compose.web.core)
                implementation(compose.runtime)
            }
        }
    }

}

/*
tasks.named<Jar>("jsJar") {
    from("build/distributions")
    into("META-INF/resources")
    dependsOn("browserDevelopmentWebpack")
}
 */
