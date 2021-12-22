package kotlinbars.desktop

import kotlinbars.ServerContainer
import kotlinbars.compose.BarsRPC

fun main() {
    val serverContainer = ServerContainer()
    serverContainer.start()

    val barsRPC = BarsRPC(serverContainer.url)
    ui(barsRPC)
}
