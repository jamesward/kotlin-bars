plugins {
    alias(universeunstable.plugins.android.application)                  apply false
    alias(universeunstable.plugins.android.library)                      apply false
    alias(universeunstable.plugins.android.kotlin.multiplatform.library) apply false
    alias(universeunstable.plugins.jetbrains.compose)                    apply false
    alias(universe.plugins.spring.boot)                                  apply false
    alias(universe.plugins.spring.dependency.management)                 apply false
    alias(universe.plugins.graalvm.buildtools.native)                    apply false
    alias(universeunstable.plugins.kotlin.android)                       apply false
    alias(universeunstable.plugins.kotlin.jvm)                           apply false
    alias(universeunstable.plugins.kotlin.multiplatform)                 apply false
    alias(universeunstable.plugins.kotlin.plugin.serialization)          apply false
    alias(universeunstable.plugins.kotlin.plugin.spring)                 apply false
}

allprojects {
    repositories {
        mavenCentral()
        google()
    }
}
