package kotlinbars

import io.micronaut.data.annotation.GeneratedValue
import io.micronaut.data.annotation.Id
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.annotation.Repository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.r2dbc.annotation.R2dbcRepository
import io.micronaut.data.repository.async.AsyncCrudRepository
import io.micronaut.data.repository.reactive.ReactiveStreamsCrudRepository
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.runtime.Micronaut
import kotlinx.coroutines.future.await
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirst
import javax.inject.Inject


fun main() {
    Micronaut.run()
}

@MappedEntity
data class Bar(@GeneratedValue @Id val id: Long, val name: String)

@R2dbcRepository(dialect = Dialect.POSTGRES)
interface BarRepository : ReactiveStreamsCrudRepository<Bar, Long>


@Controller
class WebApp(private val barRepository: BarRepository) {

    @Get("/", produces = [MediaType.TEXT_HTML])
    fun index() = run {
        println(barRepository)
        "hello, world"
    }

    @Get("/bars")
    fun getBars() = run {
        barRepository.findAll().asFlow()
    }

    @Post("/bars")
    suspend fun addBar(@Body bar: Bar): HttpResponse<Unit> = run {
        /*
        try {
            barRepository.save(bar).await()
            HttpResponse.noContent()
        } catch (e: Exception) {
            HttpResponse.serverError()
        }
         */
        TODO()
    }

}