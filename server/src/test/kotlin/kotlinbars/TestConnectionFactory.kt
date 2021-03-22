package kotlinbars

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration
import io.r2dbc.postgresql.PostgresqlConnectionFactory
import io.r2dbc.spi.ConnectionFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import org.testcontainers.containers.PostgreSQLContainer
import javax.annotation.PreDestroy

@Component
class TestPostgresContainer : PostgreSQLContainer<TestPostgresContainer>("postgres:13.1") {

    init {
        this.withInitScript("init.sql")
        this.start()
    }

    @PreDestroy
    fun destroy() {
        this.stop()
    }

}

@Configuration
class TestConnectionFactory {

    @Bean
    fun connectionFactory(container: TestPostgresContainer): ConnectionFactory {
        val connectionConfiguration = PostgresqlConnectionConfiguration.builder()
            .host(container.host)
            .port(container.firstMappedPort)
            .database(container.databaseName)
            .username(container.username)
            .password(container.password)
            .build()

        return PostgresqlConnectionFactory(connectionConfiguration)
    }

}
