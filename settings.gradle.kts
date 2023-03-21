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
    }
}
