rootProject.name = "kotlin-bars"

// when running the root jib task, ignore the android subproject
if (startParameter.taskRequests.find { it.args.contains(":server:jib") } == null) {
    include("common", "web", "server", "android")
} else {
    include("common", "web", "server")
}

pluginManagement {
    repositories {
        gradlePluginPortal()
        jcenter()
        google()
        maven("https://repo.spring.io/release")
    }
    resolutionStrategy {
        eachPlugin {
            if (requested.id.namespace == "com.android" || requested.id.name == "kotlin-android-extensions") {
                useModule("com.android.tools.build:gradle:${requested.version}")
            }
            if (requested.id.id == "kotlinx-serialization") {
                useModule("org.jetbrains.kotlin:kotlin-serialization:${requested.version}")
            }
        }
    }
}