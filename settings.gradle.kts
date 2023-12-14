pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
        google()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
    id("com.jamesward.kotlin-universe-catalog") version "2023.12.13-1"
}

rootProject.name = "kotlin-bars"

include("dev")
include("common", "rpc")
include("compose", "android", "desktop")
include("server", "web")
include("cli", "tui")
