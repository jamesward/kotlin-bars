package kotlinbars.cli

import kotlinbars.ServerContainer

fun main() {
    val serverContainer = ServerContainer()
    serverContainer.start()

    loop(serverContainer.url)
}
