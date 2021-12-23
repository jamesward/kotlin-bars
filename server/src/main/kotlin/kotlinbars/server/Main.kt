package kotlinbars.server

import kotlinbars.common.Bar
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.WebApplicationType
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.core.io.Resource
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.http.codec.json.KotlinSerializationJsonDecoder
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.await
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.config.WebFluxConfigurer
import java.net.URI
import java.util.*


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

@Configuration(proxyBeanMethods = false)
class InitConfiguration {

    @Bean
    @Profile("init")
    fun commandLineRunner(databaseClient: DatabaseClient, @Value("classpath:init.sql") initSql: Resource): CommandLineRunner {
        return CommandLineRunner {
            val lines = initSql.inputStream.bufferedReader().use { it.readText() }
            runBlocking {
                databaseClient.sql(lines).await()
            }
        }
    }

    @ExperimentalSerializationApi
    @Bean
    fun kotlinSerializationJsonDecoder() = KotlinSerializationJsonDecoder(Json {
        explicitNulls = false
    })

}

@ExperimentalSerializationApi
@Configuration
class WebConfig(val decoder: KotlinSerializationJsonDecoder) : WebFluxConfigurer {
    override fun configureHttpMessageCodecs(configurer: ServerCodecConfigurer) {
        super.configureHttpMessageCodecs(configurer)
        configurer.defaultCodecs().kotlinSerializationJsonDecoder(decoder)
    }
}

fun main(args: Array<String>) {
    val props = Properties()

    System.getenv()["DATABASE_URL"]?.let {
        val dbUri = URI(it)
        props["spring.r2dbc.url"] = "r2dbc:postgresql://" + dbUri.host + dbUri.path
        props["spring.r2dbc.username"] = dbUri.userInfo.split(":")[0]
        props["spring.r2dbc.password"] = dbUri.userInfo.split(":")[1]
    }

    runApplication<WebApp>(*args) {
        if (args.contains("init")) {
            webApplicationType = WebApplicationType.NONE
            setAdditionalProfiles("init")
            props["spring.devtools.add-properties"] = false
            props["spring.devtools.livereload.enabled"] = false
        }
        setDefaultProperties(props)
    }
}