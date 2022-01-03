package kotlinbars.server

import kotlinbars.common.Bar
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class BarRepoTest(@Autowired val barRepo: BarRepo) {

    @Test
    fun `barRepo works`(): Unit = runBlocking {
        barRepo.save(Bar(null, "foo"))

        val bars = barRepo.findAll()
        assertThat(bars.count()).isEqualTo(1)
        assertThat(bars.first().id).isNotNull
    }

}
