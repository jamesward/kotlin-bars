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

package kotlinbars.desktop

import kotlinbars.compose.Bars
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.material.MaterialTheme
import kotlinbars.rpc.BarsRPC
import java.util.*

fun ui(barsRPC: BarsRPC) {
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Bars",
        ) {
            MaterialTheme {
                Bars(barsRPC)
            }
        }
    }
}

fun main() {
    val barsUrl = object {}.javaClass.classLoader?.getResourceAsStream("META-INF/app.properties")?.use {
        val props = Properties()
        props.load(it)
        props["barsUrl"] as String?
    }

    val url = barsUrl ?: "http://localhost:8080/api/bars"

    println("Connecting to: $url")

    val barsRpc = BarsRPC(url)

    ui(barsRpc)
}
