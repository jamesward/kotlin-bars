rootProject.name = "kotlin-bars"

include("dev")
include("common", "rpc")
include("compose", "android", "desktop")
include("server", "web")
include("cli", "tui")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        //maven("https://repo.spring.io/release")
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}
