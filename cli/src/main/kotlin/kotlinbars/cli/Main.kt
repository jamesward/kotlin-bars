package kotlinbars.cli

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import kotlinbars.common.Bar
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers

fun loop(url: String) {
    val client = HttpClient.newHttpClient()
    fun requestBuilder() = HttpRequest.newBuilder().uri(URI.create(url))
    val objectMapper = jacksonObjectMapper()

    while (true) {
        val response = client.send(requestBuilder().build(), BodyHandlers.ofInputStream())
        val bars = objectMapper.readValue<List<Bar>>(response.body())

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
            val body = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsBytes(bar)
            val req = requestBuilder().POST(HttpRequest.BodyPublishers.ofByteArray(body)).header("Content-Type", "application/json").build()
            client.send(req, BodyHandlers.discarding())
            // todo: check response code
        }
    }
}

fun main(args: Array<String>) {

    val url = args.getOrNull(0) ?: "http://localhost:8080/api/bars"

    loop(url)
}
