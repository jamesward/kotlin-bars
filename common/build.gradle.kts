plugins {
    kotlin("multiplatform")
    id("com.android.library")
}

kotlin {
    android()

    jvm()

    js {
        browser()
    }
}

android {
    compileSdkVersion(30)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(24)
        targetSdkVersion(30)
    }
}