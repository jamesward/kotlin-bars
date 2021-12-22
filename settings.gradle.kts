rootProject.name = "kotlin-bars"

include("common", "rpc", "web", "server", "cli", "dev")

// todo
if (startParameter.taskRequests.find { it.args.contains(":server:jib") } == null) {
    include("compose", "android", "desktop")
}

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven("https://repo.spring.io/release")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.namespace == "com.android" || requested.id.name == "kotlin-android-extensions") {
                useModule("com.android.tools.build:gradle:${requested.version}")
            }
        }
    }
}