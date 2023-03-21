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

package kotlinbars.tui

import kotlinbars.common.Bar
import kotlinbars.rpc.BarsRPC
import com.varabyte.kotter.foundation.input.*
import com.varabyte.kotter.foundation.liveVarOf
import com.varabyte.kotter.foundation.session
import com.varabyte.kotter.foundation.text.*
import kotlinx.coroutines.runBlocking

fun main() = session {
    val rpc = BarsRPC(Config.barsUrl)

    var bars by liveVarOf<List<Bar>>(emptyList())

    run {
        section {
            textLine("Connecting to: ${Config.barsUrl}")
        }
    }.run {
        bars = rpc.fetchBars()
    }

    run {
        section {
            if (bars.isEmpty()) {
                textLine("No Bars")
            } else {
                textLine("Bars:")
                bars.forEach {
                    textLine("  ${it.name}")
                }
            }

            textLine()
            text("Create a Bar: ")
            input()
        }.runUntilKeyPressed(Keys.ESC) {
            onInputEntered {
                clearInput()
                runBlocking {
                    rpc.addBar(Bar(null, input))
                    bars = rpc.fetchBars()
                }
            }
        }
    }
}

