package kotlinbars

import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

interface BarRepo : ReactiveCrudRepository<Bar, Long>

@SpringBootApplication
@RestController
class WebApp(val barRepo: BarRepo) {

    @GetMapping("/api/bars")
    suspend fun getBars() = run {
        barRepo.findAll().collectList().awaitFirst()
    }

    @PostMapping("/api/bars")
    suspend fun addBar(@RequestBody bar: Bar) = run {
        barRepo.save(bar).awaitFirstOrNull()?.let {
            ResponseEntity<Unit>(HttpStatus.NO_CONTENT)
        } ?: ResponseEntity<Unit>(HttpStatus.INTERNAL_SERVER_ERROR)
    }

}

fun main(args: Array<String>) {
    runApplication<WebApp>(*args)
}