package kotlinbars.desktop

import kotlinbars.ServerContainer
import kotlinbars.rpc.BarsRPC

fun main() {
    val serverContainer = ServerContainer()
    serverContainer.start()

    val barsRPC = BarsRPC(serverContainer.url)
    ui(barsRPC)
}
