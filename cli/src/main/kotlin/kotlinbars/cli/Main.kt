package kotlinbars.cli

import kotlinbars.common.Bar
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers
import kotlinx.serialization.json.Json
import java.util.*

fun loop(url: String) {
    val client = HttpClient.newHttpClient()
    fun requestBuilder() = HttpRequest.newBuilder().version(HttpClient.Version.HTTP_1_1).uri(URI.create(url))

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

fun main() {

    val barsUrl = object {}.javaClass.classLoader?.getResourceAsStream("META-INF/app.properties")?.use {
        val props = Properties()
        props.load(it)
        props["barsUrl"] as String?
    }

    val url = barsUrl ?: "http://localhost:8080/api/bars"

    println("Connecting to: $url")

    loop(url)
}
