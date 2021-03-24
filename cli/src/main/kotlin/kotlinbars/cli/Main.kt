package kotlinbars.cli

import kotlinbars.common.Bar
import kotlinx.coroutines.runBlocking
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBodilessEntity
import org.springframework.web.reactive.function.client.awaitBody

@SpringBootApplication
class BarsCommandLine : CommandLineRunner {

    val webClient = WebClient.create()

    override fun run(vararg args: String?): Unit = runBlocking {
        val url = args.getOrNull(0) ?: "http://localhost:8080/api/bars"

        while (true) {
            val bars = webClient.get().uri(url).retrieve().awaitBody<List<Bar>>()

            if (bars.isEmpty()) {
                println("\nNo Bars")
            }
            else {
                println("\nBars:")
                bars.forEach { println("  ${it.name}") }
            }

            print("\nCreate a Bar: ")
            val name = readLine()
            if (!name.isNullOrEmpty()) {
                webClient.post().uri(url).bodyValue(Bar(null, name)).retrieve().awaitBodilessEntity()
            }
        }
    }
}

fun main(args: Array<String>) {
    runApplication<BarsCommandLine>(*args)
}