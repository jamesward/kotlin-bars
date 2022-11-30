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

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.web.reactive.function.client.WebClient
import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.awaitBodilessEntity

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WebAppTest(@LocalServerPort val randomServerPort: Int) {

    val webClient = WebClient.create()

    @Test
    fun `bar deserialization works without an id`(): Unit = runBlocking {
        val json = """
            |{
            |  "name": "test"
            |}
        """.trimMargin()

        val uri = "http://localhost:$randomServerPort/api/bars"
        val resp = webClient.post().uri(uri).contentType(MediaType.APPLICATION_JSON).bodyValue(json).retrieve().awaitBodilessEntity()
        assertThat(resp.statusCode).isEqualTo(HttpStatus.NO_CONTENT)
    }

}