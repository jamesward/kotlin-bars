package kotlinbars.desktop

import kotlinbars.compose.Bars
import androidx.compose.desktop.Window
import java.util.*

fun ui(url: String) {
    Window {
        Bars(url)
    }
}

fun main() {
    val barsUrl = object {}.javaClass.classLoader.getResourceAsStream("META-INF/app.properties")?.use {
        val props = Properties()
        props.load(it)
        props["barsUrl"] as String?
    }

    val url = barsUrl ?: "http://localhost:8080/api/bars"

    println("Connecting to: $url")

    ui(url)
}
