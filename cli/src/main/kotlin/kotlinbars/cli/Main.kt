package kotlinbars.cli

import kotlinbars.common.Bar
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers
import kotlinx.serialization.json.Json

fun loop(url: String) {
    val client = HttpClient.newHttpClient()
    fun requestBuilder() = HttpRequest.newBuilder().uri(URI.create(url))

    while (true) {
        val response = client.send(requestBuilder().build(), BodyHandlers.ofString())
        val bars = Json.decodeFromString<List<Bar>>(response.body())

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
            val body = Json.encodeToString(bar)
            val req = requestBuilder().POST(HttpRequest.BodyPublishers.ofString(body)).header("Content-Type", "application/json").build()
            client.send(req, BodyHandlers.discarding())
            // todo: check response code
        }
    }
}

fun main(args: Array<String>) {

    val url = args.getOrNull(0) ?: "http://localhost:8080/api/bars"

    loop(url)
}
