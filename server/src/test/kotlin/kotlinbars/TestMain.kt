package kotlinbars

import io.ktor.application.*
import io.ktor.server.netty.*
import org.testcontainers.containers.PostgreSQLContainer


@Suppress("unused")
fun Application.testModule() {

    class PostgresContainer : PostgreSQLContainer<PostgresContainer>("postgres:13.1")

    val container by lazy {
        val underlying = PostgresContainer().withInitScript("init.sql")
        underlying.start()
        underlying
    }

    baseModule {
        install(ConnectionFeature) {
            host = container.host
            port = container.firstMappedPort
            database = container.databaseName
            username = container.username
            password = container.password
        }
        environment.monitor.subscribe(ApplicationStopped) {
            container.stop()
        }
    }

}

fun main(args: Array<String>) {
    EngineMain.main(args)
}