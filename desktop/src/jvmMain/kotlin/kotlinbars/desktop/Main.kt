package kotlinbars.desktop

import kotlinbars.compose.Bars
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.material.MaterialTheme
import kotlinbars.compose.BarsRPC
import java.util.*

fun ui(barsRPC: BarsRPC) {
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Bars",
        ) {
            MaterialTheme {
                Bars(barsRPC)
            }
        }
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

    val barsRpc = BarsRPC(url)

    ui(barsRpc)
}
