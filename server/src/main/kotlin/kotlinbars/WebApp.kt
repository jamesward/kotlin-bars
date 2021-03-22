package kotlinbars

import com.github.jasync.sql.db.SuspendingConnection
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.runtime.Micronaut

fun main() {
    Micronaut.run()
}

@Controller
class WebApp(private val connection: SuspendingConnection) {

    @Get("/api/bars")
    suspend fun getBars() = run {
        connection.sendQuery("SELECT * FROM bar").rows.flatMap { row ->
            row.getString("name")?.let {
                listOf(Bar(it))
            } ?: emptyList()
        }
    }

    @Post("/api/bars")
    suspend fun addBar(@Body bar: Bar): HttpResponse<Unit> = run {
        val sql = "INSERT INTO BAR (name) VALUES (?)"
        val numRows = connection.sendPreparedStatement(sql, listOf(bar.name)).rowsAffected
        if (numRows == 1L)
            HttpResponse.noContent()
        else
            HttpResponse.serverError()
    }

}