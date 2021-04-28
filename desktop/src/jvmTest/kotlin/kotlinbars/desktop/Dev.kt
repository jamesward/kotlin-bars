package kotlinbars.desktop

import kotlinbars.ServerContainer

fun main() {
    val serverContainer = ServerContainer()
    serverContainer.start()

    ui(serverContainer.url)
}
