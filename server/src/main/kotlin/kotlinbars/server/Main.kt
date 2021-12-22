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
    runApplication<WebApp>(*args) {
        if (args.contains("init")) {
            webApplicationType = WebApplicationType.NONE
            setAdditionalProfiles("init")
        }
    }
}