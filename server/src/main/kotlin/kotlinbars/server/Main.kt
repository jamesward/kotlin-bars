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
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.http.codec.json.KotlinSerializationJsonDecoder
import org.springframework.web.bind.annotation.*
import org.springframework.web.reactive.config.WebFluxConfigurer
import java.net.URI
import java.util.*

interface BarRepo : CoroutineCrudRepository<Bar, Long>

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
