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
