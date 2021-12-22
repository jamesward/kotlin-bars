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
import androidx.compose.runtime.*
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