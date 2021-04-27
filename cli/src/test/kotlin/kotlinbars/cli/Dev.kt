package kotlinbars.cli


fun main() {

/*
docker run -it --network host \
  -eSPRING_R2DBC_URL=r2dbc:postgresql://localhost/postgres \
  -eSPRING_R2DBC_USERNAME=postgres \
  -eSPRING_R2DBC_PASSWORD=password \
 */

    /*

    val network = Network.newNetwork()

     class PostgresContainer extends PostgreSQLContainer[PostgresContainer]("postgres:13.1")

  val postgresContainer = new PostgresContainer()
    .withInitScript("init.sql")
    .withNetwork(network)
    .withNetworkAliases("postgres")

  postgresContainer.start()

  class KtorContainer extends GenericContainer[KtorContainer]("ktor-server")

  val ktorContainer = new KtorContainer()
    .withExposedPorts(8080)
    .withNetwork(network)
    .waitingFor(Wait.forHttp("/bars"))
    .withEnv("JASYNC_CLIENT_HOST", "postgres")
    .withEnv("JASYNC_CLIENT_PORT", "5432")
    .withEnv("JASYNC_CLIENT_DATABASE", postgresContainer.getDatabaseName)
    .withEnv("JASYNC_CLIENT_USERNAME", postgresContainer.getUsername)
    .withEnv("JASYNC_CLIENT_PASSWORD", postgresContainer.getPassword)
     */




}

/*
class TestPostgresContainer : PostgreSQLContainer<TestPostgresContainer>("postgres:13.1") {

    init {
        withInitScript("init.sql")
        start()
    }

    @PreDestroy
    fun destroy() {
        stop()
    }

}
 */
