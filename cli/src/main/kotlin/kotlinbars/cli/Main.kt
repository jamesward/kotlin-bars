package kotlinbars.cli

import kotlinbars.common.Bar
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers


fun main(args: Array<String>) {

    val client = HttpClient.newHttpClient()

    val url = args.getOrNull(0) ?: "http://localhost:8080/api/bars"

    val request = HttpRequest.newBuilder().uri(URI.create(url)).build()

    while (true) {
        val response = client.send(request, BodyHandlers.ofString())
        println(response.statusCode())
        println(response.body())

        /*

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

         */
        readLine()
    }
}
