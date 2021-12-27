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

    /*
    sourceSets["main"].dependencies {
        implementation(project(":compose"))
                /*
                implementation(compose.runtime)
                implementation(compose.web.widgets)
                implementation(project(":common:utils"))
                implementation(project(":common:database"))
                implementation(project(":common:root"))
                implementation(project(":common:main"))
                implementation(project(":common:edit"))
                implementation(Deps.ArkIvanov.Decompose.decompose)
                implementation(Deps.ArkIvanov.MVIKotlin.mvikotlin)
                implementation(Deps.ArkIvanov.MVIKotlin.mvikotlinMain)
                implementation(npm("copy-webpack-plugin", "9.0.0"))
                implementation(npm("@material-ui/icons", "4.11.2"))
                 */
    }
     */

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
plugins {
    kotlin("js")
}
 */

/*
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
 */

/*
tasks.named<Jar>("jsJar") {
    from("build/distributions")
    into("META-INF/resources")
    dependsOn("browserDevelopmentWebpack")
}
 */
