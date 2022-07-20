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

package kotlinbars

import org.slf4j.LoggerFactory
import org.testcontainers.containers.GenericContainer
import org.testcontainers.containers.Network
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.containers.output.Slf4jLogConsumer
import org.testcontainers.containers.wait.strategy.Wait

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
