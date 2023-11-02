pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
    id("com.jamesward.kotlin-universe-catalog") version "2023.11.01-4"
}

rootProject.name = "kotlin-bars"

include("dev")
include("common", "rpc")
include("compose", "android", "desktop")
include("server", "web")
include("cli", "tui")
