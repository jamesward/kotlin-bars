package kotlinbars.cli

import org.slf4j.LoggerFactory
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.Network
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.containers.wait.strategy.Wait

fun main() {
    val serverContainer = ServerContainer()
    serverContainer.start()

    loop(serverContainer.url)
}

class TestPostgresContainer : PostgreSQLContainer<TestPostgresContainer>("postgres:13.1") {
    init {
        withInitScript("init.sql")
    }
}

class ServerContainer : GenericContainer<ServerContainer>("server") {

    val logger = LoggerFactory.getLogger(this.javaClass)

    private val myNetwork = Network.newNetwork()

    override fun getNetwork(): Network {
        return myNetwork
    }

    private val testPostgresContainer = TestPostgresContainer().withNetwork(myNetwork).withNetworkAliases("postgres")

    init {
        withExposedPorts(8080)
        withNetwork(myNetwork)
        withLogConsumer(Slf4jLogConsumer(logger))
        waitingFor(Wait.forHttp("/actuator/health"))
        dependsOn(testPostgresContainer)
    }

    override fun configure() {
        super.configure()
        withEnv("SPRING_R2DBC_URL", "r2dbc:postgresql://postgres/${testPostgresContainer.databaseName}")
        withEnv("SPRING_R2DBC_USERNAME", testPostgresContainer.username)
        withEnv("SPRING_R2DBC_PASSWORD", testPostgresContainer.password)
    }

    val url by lazy { "http://${host}:${firstMappedPort}/api/bars" }

}