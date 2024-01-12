plugins {
    //`java-library`
    //alias(universe.plugins.kotlin.jvm)
    alias(universe.plugins.kotlin.multiplatform)
}

kotlin {
    jvmToolchain(11)

    jvm()

    sourceSets {
        jvmMain {
            dependencies {
                api(universe.testcontainers.postgresql)
                runtimeOnly(universe.slf4j.simple)
                runtimeOnly(universe.postgresql)
            }
        }
    }
}
