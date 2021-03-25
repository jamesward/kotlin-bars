package kotlinbars.cli

import kotlinbars.common.Bar
import kotlinx.coroutines.runBlocking
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.nativex.hint.MethodHint
import org.springframework.nativex.hint.TypeHint
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.awaitBodilessEntity
import org.springframework.web.reactive.function.client.awaitBody

// https://github.com/spring-projects-experimental/spring-native/issues/663
@TypeHint(
    types = [SpringApplication::class],
    methods = [MethodHint(name = "setWebApplicationType", parameterTypes = [WebApplicationType::class])]
)
@SpringBootApplication
class BarsCommandLine : CommandLineRunner {

    val webClient = WebClient.create()

    override fun run(vararg args: String?): Unit = runBlocking {
        val url = args.getOrNull(0) ?: "http://localhost:8080/api/bars"

        while (true) {
            val bars = webClient.get().uri(url).retrieve().awaitBody<List<Bar>>()

            if (bars.isEmpty()) {
                println("\nNo Bars")
            } else {
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