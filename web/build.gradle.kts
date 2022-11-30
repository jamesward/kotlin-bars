plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

kotlin {
    js(IR) {
        browser {
            runTask {
                // todo: testcontainer server
                devServer = devServer?.copy(port = 8081, proxy = mutableMapOf("/api" to "http://localhost:8080"))
            }
        }
        binaries.executable()
    }

    sourceSets {
        named("jsMain") {
            dependencies {
                implementation(project(":rpc"))
                implementation(compose.web.core)
                implementation(compose.runtime)
            }
        }
    }

}

// workaround for https://youtrack.jetbrains.com/issue/KT-52776
rootProject.extensions.configure<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension> {
    versions.webpackCli.version = "4.10.0"
}

/*
tasks.named<Jar>("jsJar") {
    from("build/distributions")
    into("META-INF/resources")
    dependsOn("browserDevelopmentWebpack")
}
 */
