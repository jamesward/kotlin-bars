package kotlinbars.rpc

import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinbars.common.Bar

class BarsRPC(private val barsUrl: String) {

    private val client = HttpClient {
        install(JsonFeature)
    }

    suspend fun fetchBars(): List<Bar> {
        return client.get(barsUrl)
    }

    suspend fun addBar(bar: Bar) {
        return client.post {
            url(barsUrl)
            contentType(ContentType.Application.Json)
            body = bar
        }
    }

    fun close() {
        client.close()
    }

}
