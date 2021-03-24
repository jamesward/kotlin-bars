package kotlinbars.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.github.kittinunf.fuel.coroutines.awaitObjectResult
import kotlinbars.Bar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
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

    val scope = remember { CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate) }

    scope.launch {
        Fuel.get(url).awaitObjectResult(BarDeserializer).fold(
            {bars -> println(bars)},
            {error -> println(error)}
        )
    }

    Text("Hello World!")
}
