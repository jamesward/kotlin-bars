package kotlinbars.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
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
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.coroutines.awaitObjectResult
import com.github.kittinunf.fuel.coroutines.awaitStringResponse
import com.github.kittinunf.fuel.jackson.objectBody
import kotlinbars.Bar
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val url = resources.getString(R.string.bars_url) + "/api/bars"

        setContent {
            Bars(url)
        }
    }
}

object BarDeserializer : ResponseDeserializable<List<Bar>> {
    override fun deserialize(content: String) = jacksonObjectMapper().readValue<List<Bar>>(content)
}

@Composable
fun Bars(url: String) {

    val barsState = remember { mutableStateListOf<Bar>() }

    val nameState = remember { mutableStateOf(TextFieldValue()) }

    val scope = rememberCoroutineScope()

    suspend fun fetchBars() {
        Fuel.get(url).awaitObjectResult(BarDeserializer).fold(
            { bars ->
                barsState.clear()
                barsState.addAll(bars)
            },
            { error ->
                // todo: toast
                println(error)
            }
        )
    }

    LaunchedEffect(Unit) {
        fetchBars()
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
                val (_, res, _) = Fuel.post(url).objectBody(Bar(null, nameState.value.text)).awaitStringResponse()
                if (res.statusCode == 204) {
                    nameState.value = TextFieldValue()
                    fetchBars()
                }
                else {
                    println("Error")
                }
            }
        }, Modifier.padding(top = 10.dp)) {
            Text("Add Bar")
        }
    }

}
