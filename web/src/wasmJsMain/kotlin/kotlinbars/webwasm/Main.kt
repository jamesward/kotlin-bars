package kotlinbars.webwasm

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.CanvasBasedWindow
import kotlinbars.compose.Bars
import kotlinbars.rpc.BarsRPC

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    val barsRpc = BarsRPC("/api/bars")

    CanvasBasedWindow("KotlinBars", "kotlinbarsCanvas") {
        MaterialTheme {
            Bars(barsRpc)
        }
    }
}