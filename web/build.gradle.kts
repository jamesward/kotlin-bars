plugins {
    kotlin("js")
}

repositories {
    maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/kotlin-js-wrappers")
}

kotlin {
    js {
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
            runTask {
                devServer = devServer?.copy(port = 8081, proxy = mapOf("/api" to "http://localhost:8080"))
            }
        }
        binaries.executable()
    }
    sourceSets["main"].dependencies {
        implementation(kotlin("stdlib-js"))
        implementation("org.jetbrains:kotlin-extensions:1.0.1-pre.155-kotlin-1.4.32")
        implementation(project(":common"))
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core-js:1.4.3")
        implementation("org.jetbrains.kotlinx:kotlinx-html-js:0.7.3")

        implementation(npm("bootstrap", "4.6.0"))
    }
}

/*
tasks.named<Jar>("jsJar") {
    from("build/distributions")
    into("META-INF/resources")
    dependsOn("browserDevelopmentWebpack")
}
 */
