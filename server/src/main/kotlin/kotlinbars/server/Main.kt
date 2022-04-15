package kotlinbars.server

import kotlinbars.common.Bar
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.http.codec.json.KotlinSerializationJsonDecoder
import org.springframework.nativex.hint.MethodHint
import org.springframework.nativex.hint.TypeAccess
import org.springframework.nativex.hint.TypeHint
import org.springframework.r2dbc.core.DatabaseClient
import org.springframework.r2dbc.core.await
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.config.WebFluxConfigurer
import java.net.URI
import java.util.*

interface BarRepo : CoroutineCrudRepository<Bar, Long>

@TypeHint(types = [kotlinx.serialization.encoding.CompositeEncoder::class, kotlinx.serialization.descriptors.SerialDescriptor::class])
@TypeHint(types = [Object::class])
@TypeHint(types = [Bar::class], access = [TypeAccess.DECLARED_FIELDS, TypeAccess.QUERY_DECLARED_METHODS, TypeAccess.QUERY_PUBLIC_METHODS, TypeAccess.QUERY_DECLARED_CONSTRUCTORS])
@TypeHint(typeNames = ["kotlinbars.common.Bar\$\$serializer"])
@TypeHint(typeNames = ["kotlinbars.common.Bar\$Companion"], methods = [MethodHint(name = "serializer", parameterTypes = [])])
@SpringBootApplication
@RestController
class WebApp(val barRepo: BarRepo) {

    @GetMapping("/api/bars")
    suspend fun getBars() = run {
        barRepo.findAll()
    }

    @PostMapping("/api/bars")
    suspend fun addBar(@RequestBody bar: Bar) = run {
        barRepo.save(bar)
        ResponseEntity<Unit>(HttpStatus.NO_CONTENT)
    }

    @DeleteMapping("/api/bars/{id}")
    suspend fun deleteBar(@PathVariable id: Long) = run {
        barRepo.deleteById(id)
        ResponseEntity<Unit>(HttpStatus.OK)
    }

}

@Component
class InitDB(val databaseClient: DatabaseClient, @Value("classpath:init.sql") val initSql: Resource) : CommandLineRunner {

    private val logger = LoggerFactory.getLogger(InitDB::class.java)

    override fun run(vararg args: String?) {
        if (args.contains("init")) {
            logger.info("Init DB Schema")
            val lines = initSql.inputStream.bufferedReader().use { it.readText() }
            runBlocking {
                databaseClient.sql(lines).await()
            }
        }
    }

}

@Configuration(proxyBeanMethods = false)
class JsonConfig {

    @ExperimentalSerializationApi
    @Bean
    fun kotlinSerializationJsonDecoder() = KotlinSerializationJsonDecoder(Json {
        explicitNulls = false
    })

    @Bean
    fun webConfig(decoder: KotlinSerializationJsonDecoder): WebFluxConfigurer {
        return object : WebFluxConfigurer {
            override fun configureHttpMessageCodecs(configurer: ServerCodecConfigurer) {
                super.configureHttpMessageCodecs(configurer)
                configurer.defaultCodecs().kotlinSerializationJsonDecoder(decoder)
            }
        }
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
        setDefaultProperties(props)
    }
}