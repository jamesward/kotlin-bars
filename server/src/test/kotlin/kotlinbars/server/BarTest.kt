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

import kotlinbars.common.Bar
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.core.ResolvableType
import org.springframework.core.codec.Decoder
import org.springframework.core.io.buffer.DefaultDataBufferFactory


@SpringBootTest
class BarTest(@Autowired val decoder: Decoder<Any>) {

    @Test
    fun `bar deserialization works without an id`(): Unit = runBlocking {
        val json = """
            |{
            |  "name": "test"
            |}
        """.trimMargin()

        val dataBufferFactory = DefaultDataBufferFactory()
        val dataBuffer = dataBufferFactory.wrap(json.encodeToByteArray())

        val bar = decoder.decode(dataBuffer, ResolvableType.forClass(Bar::class.java), null, null) as Bar?
        assert(bar?.id == null)
        assert(bar?.name == "test")
    }

}
