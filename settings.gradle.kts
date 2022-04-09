rootProject.name = "kotlin-bars"

include("common", "rpc", "web", "server", "cli", "dev", "tui")

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
}
