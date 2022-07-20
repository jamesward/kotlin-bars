/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kotlinbars.server

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
        withInitScript("init.sql")
        start()
    }

    @PreDestroy
    fun destroy() {
        stop()
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
