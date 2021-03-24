package kotlinbars.desktop

import kotlinbars.compose.Bars
import androidx.compose.desktop.Window


fun main(args: Array<String>) {
    val url = args.getOrNull(0) ?: "http://localhost:8080/api/bars"

    Window {
        Bars(url)
    }
}
