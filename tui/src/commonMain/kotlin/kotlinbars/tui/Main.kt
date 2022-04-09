package kotlinbars.tui

import kotlinbars.common.Bar
import kotlinbars.rpc.BarsRPC
import kotlinx.coroutines.runBlocking


suspend fun loop(url: String) {
    val rpc = BarsRPC(url)

    while (true) {
        val bars = rpc.fetchBars()

        if (bars.isEmpty()) {
            println("\nNo Bars")
        } else {
            println("\nBars:")
            bars.forEach { println("  ${it.name}") }
        }

        print("\nCreate a Bar: ")
        val name = readLine()
        if (!name.isNullOrEmpty()) {
            val bar = Bar(null, name)
            rpc.addBar(bar)
            // todo: check response code
        }
    }
}

fun main() = runBlocking {
    val url = "https://kotlinbars.jamesward.com/api/bars"

    println("Connecting to: $url")

    loop(url)
}

