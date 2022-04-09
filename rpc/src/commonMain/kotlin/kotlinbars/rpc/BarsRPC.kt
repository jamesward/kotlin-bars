package kotlinbars.rpc

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinbars.common.Bar

class BarsRPC(private val barsUrl: String) {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    suspend fun fetchBars(): List<Bar> {
        return client.get(barsUrl).body()
    }

    suspend fun addBar(bar: Bar) {
        return client.post(barsUrl) {
            contentType(ContentType.Application.Json)
            setBody(bar)
        }.body()
    }

    fun close() {
        client.close()
    }

}
