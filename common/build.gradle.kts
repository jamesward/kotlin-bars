plugins {
    kotlin("multiplatform")
}

kotlin {
    jvm()

    js {
        browser()
    }
}
