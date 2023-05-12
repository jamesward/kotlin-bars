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

package kotlinbars.compose

import androidx.compose.runtime.*
import kotlinbars.common.Bar
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import kotlinbars.rpc.BarsRPC

@Composable
fun Bars(barsRpc: BarsRPC) {
    val barsState = remember { mutableStateListOf<Bar>() }

    val nameState = remember { mutableStateOf(TextFieldValue()) }

    val scope = rememberCoroutineScope()

    suspend fun updateBars() {
        val bars = barsRpc.fetchBars()
        barsState.clear()
        barsState.addAll(bars)
    }

    LaunchedEffect(Unit) {
        updateBars()
    }

    Column(Modifier.fillMaxWidth().fillMaxHeight().padding(10.dp), Arrangement.Top) {
        Text("Bars:")

        LazyColumn {
            items(barsState.toList(), { it.id!! }) { bar ->
                Text(bar.name, modifier = Modifier.padding(5.dp))
            }
        }

        OutlinedTextField(nameState.value, { nameState.value = it })

        Button({
            scope.launch {
                val bar = Bar(null, nameState.value.text)

                try {
                    barsRpc.addBar(bar)
                    nameState.value = TextFieldValue()
                    updateBars()
                } catch (e: Exception) {
                    println(e.message)
                }
            }
        }, Modifier.padding(top = 10.dp)) {
            Text("Add Bar")
        }
    }
}
