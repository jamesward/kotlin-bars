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

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import io.ktor.http.*
import kotlinbars.common.Bar
import org.jetbrains.compose.web.dom.*
import org.jetbrains.compose.web.renderComposable
import kotlinbars.rpc.BarsRPC
import kotlinx.browser.window
import kotlinx.coroutines.launch

@Composable
fun Bars(barsRPC: BarsRPC) {

    val barsState = remember { mutableStateListOf<Bar>() }

    val nameState = remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    suspend fun updateBars() {
        val bars = barsRPC.fetchBars()
        barsState.clear()
        barsState.addAll(bars)
    }

    LaunchedEffect(Unit) {
        updateBars()
    }

    Div {
        Text("Bars:")
        Ul {
            barsState.forEach {
                Li {
                    Text(value = it.name)
                }
            }
        }
    }

    fun createBar() {
        scope.launch {
            val bar = Bar(null, nameState.value)

            try {
                barsRPC.addBar(bar)
                nameState.value = ""
                updateBars()
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

    Div {
        TextInput(nameState.value) {
            onInput {
                nameState.value = it.value
            }
            onKeyUp {
                if (it.key === "Enter") createBar()
            }
        }

        Button(
            {
                onClick { createBar() }
            }
        ) {
            Text("Add Bar")
        }
    }

}

fun main() {
    val barsUrl = URLBuilder(window.location.href).takeFrom("/api/bars").build()

    val barsRpc = BarsRPC(barsUrl.toString())

    renderComposable(rootElementId = "root") {
        Bars(barsRpc)
    }
}
