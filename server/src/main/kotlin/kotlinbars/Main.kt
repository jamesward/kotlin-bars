package kotlinbars

import com.github.jasync.sql.db.ConnectionPoolConfigurationBuilder
import com.github.jasync.sql.db.SuspendingConnection
import com.github.jasync.sql.db.asSuspending
import com.github.jasync.sql.db.postgresql.PostgreSQLConnectionBuilder
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.jackson.*
import io.ktor.server.netty.*
import io.ktor.util.*

class ConnectionFeature {
    companion object : ApplicationFeature<Application, ConnectionPoolConfigurationBuilder, SuspendingConnection> {
        override val key: AttributeKey<SuspendingConnection> = AttributeKey("Connection")

        override fun install(pipeline: Application, configure: ConnectionPoolConfigurationBuilder.() -> Unit): SuspendingConnection {
            val config = ConnectionPoolConfigurationBuilder().apply { configure() }
            val pool = PostgreSQLConnectionBuilder.createConnectionPool(config)

            pipeline.intercept(ApplicationCallPipeline.Call) {
                this.context.attributes.put(key, pool.asSuspending)
            }

            return pool.asSuspending
        }
    }
}

fun Application.baseModule(other: () -> Any = {}) {
    other()

    install(DefaultHeaders)
    install(CallLogging)
    install(ContentNegotiation) {
        jackson()
    }
    install(Routing) {
        route("/api") {
            route("/bars") {
                get {
                    call.attributes.getOrNull(ConnectionFeature.key)?.let { client ->
                        val bars = client.sendQuery("SELECT * FROM bar").rows.flatMap { row ->
                            row.getString("name")?.let {
                                listOf(Bar(it))
                            } ?: emptyList()
                        }
                        call.respond(bars)
                    }
                }
                post {
                    val bar = call.receive<Bar>()
                    call.attributes.getOrNull(ConnectionFeature.key)?.let { client ->
                        val sql = "INSERT INTO BAR (name) VALUES (?)"
                        val numRows = client.sendPreparedStatement(sql, listOf(bar.name)).rowsAffected
                        if (numRows == 1L)
                            call.respond(HttpStatusCode.NoContent)
                        else
                            call.respond(HttpStatusCode.InternalServerError)
                    }
                }
            }
        }
    }
}

@Suppress("unused")
fun Application.prodModule() {
    baseModule {
        install(ConnectionFeature) {
            host = System.getenv("JASYNC_CLIENT_HOST")
            port = System.getenv("JASYNC_CLIENT_PORT").toInt()
            database = System.getenv("JASYNC_CLIENT_DATABASE")
            username = System.getenv("JASYNC_CLIENT_USERNAME")
            password = System.getenv("JASYNC_CLIENT_PASSWORD")
        }
    }
}

fun main(args: Array<String>) {
    EngineMain.main(args)
}